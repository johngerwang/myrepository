package com.wang.concurrency.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 本例子使用RecursiveTask类，获得任务的返回结果
 * 功能说明:
 * 1.Document：生成二维的字符串数组，供task去查询
 * 2.DocumentTask：从上面生成的二维数组中查询指定的元素，如果查询的行数大于指定的值(Reference Size)，
 * 那么拆分，直到每一个任务的查询的行数<指定值，才可以查询。递归。
 * 3.LineTask：由DocumentTask调用，每一行一个LineTask。LineTask用于查询指定行中的指定元素。每一行中元素的个数如果超过指定的数量，则也拆分。
 * DcoumentTask是从行的角度拆分，LineTask是从列的角度拆分。
 */


public class ForkJoin_RecursiveTask {

	public static void main(String[] args) {

		Document doc = new Document();
		String[][] document = doc.generate(1, 10, "class");
		DocumentTask dt = new DocumentTask(0, document.length, "class", document);
		ForkJoinPool fjp = new ForkJoinPool();
		fjp.submit(dt);
		// do{
		// System.out.println("Main ForkJoinPool.getParallelism():
		// "+fjp.getParallelism());
		// System.out.println("Main ForkJoinPool.getActiveThreadCount(): "
		// +fjp.getActiveThreadCount());
		// System.out.println("Main ForkJoinPool.getStealCount(): "
		// +fjp.getStealCount());
		// System.out.println("Main ForkJoinPool.getQueuedTaskCount()
		// "+fjp.getQueuedTaskCount());
		// try {
		// TimeUnit.MILLISECONDS.sleep(5);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }while(!dt.isDone());
		// fjp.shutdown();
		try {
			System.out.println("DocumentTask Result: " + dt.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}

class Document {

	private String words[] = { "package", "public", "class", "implemets", "break" };

	public String[][] generate(int row, int column, String word) {
		Random random = new Random();
		String[][] document = new String[row][column];
		int counter = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				String randomWord = words[random.nextInt(words.length - 1)];
				document[i][j] = randomWord;
				if (randomWord.equals(word)) {
					counter++;
				}
			}
		}
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				System.out.printf("%s\t", document[i][j]);
				if (j == (column-1)) {
					System.out.println("");
				}
			}
		}
		System.out.println("Document: the word appears " + (counter) + " times");

		return document;
	}
}

class DocumentTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = 1L;

	private int start;
	private int end;
	private String word;
	private String[][] document;

	public DocumentTask(int start, int end, String word, String[][] document) {
		this.document = document;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute() {
		int result = 0;
		if (end - start < 5) {
			result = processLine(start, end, word, document);
		} else {
			int middle = (end + start) / 2;
			DocumentTask dt1 = new DocumentTask(start, middle, word, document);
			DocumentTask dt2 = new DocumentTask(middle, end, word, document);
			invokeAll(dt1, dt2);// 同步的。所有的DocumentTask都完成后才能往下。
			try {
				Integer result1 = dt1.get();
				//System.out.println("DocumentTask.compute(1): " + result1);
				Integer result2 = dt2.get();
				//System.out.println("DocumentTask compute(2): " + result2);
				result = group(result1, result2);
				//System.out.println("DocumentTask.compute(3): " + result);
//try代码段总共执行了3遍。第一遍的结果是3，3，加起来等6。第二遍是1，3，加起来等4。第三遍是用第一遍的和+第二遍的和相加。
//				DocumentTask.compute(1): 3
//				DocumentTask compute(2): 3
//				DocumentTask.compute(3): 6
//				DocumentTask.compute(1): 1
//				DocumentTask compute(2): 3
//				DocumentTask.compute(3): 4
//				DocumentTask.compute(1): 6
//				DocumentTask compute(2): 4
//				DocumentTask.compute(3): 10
//				DocumentTask Result: 10
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;// int->integer自动装箱
	}

	private int group(Integer result1, Integer result2) {
		return result1 + result2;
	}

	private Integer processLine(int start, int end, String word, String[][] document) {
		int result = 0;
		List<LineTask> tasks = new ArrayList<LineTask>();
		// 每一行一个task去执行查找
		for (int i = start; i < end; i++) {
			LineTask task = new LineTask(0, document[i].length, word, document[i]);
			tasks.add(task);
		}
		invokeAll(tasks);
		for (int i = 0; i < tasks.size(); i++) {
			try {
				int result1 = tasks.get(i).get();
				//System.out.println("#" + i + " DocumentTask.processLine(1) : " + result1);
				result = result + result1;// 等待完成后才能获得
				//System.out.println("#" + i + " DocumentTask.processLine(2) : " + result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("End DocumentTask.processLine(2): " + result);
		return result;
	}
	
//	叶子节点，总共有10行，分成4个叶子节点
//	#0 DocumentTask.processLine(1) : 1                    A（主任务，DocumentTask)
//	#0 DocumentTask.processLine(1) : 1                 /     \
//	#1 DocumentTask.processLine(1) : 1      line 0-4 A1        A2(line5-9) 两个子任务(DocumentTask)
//	#1 DocumentTask.processLine(1) : 2              /   \     /   \    
//	End DocumentTask.processLine(2): 2             A11  A12  A21  A22     再拆分成4个子任务(DocumentTask)
//	End DocumentTask.processLine(2): 3   line     0-2   3-4  5-6  7-9
//	#0 DocumentTask.processLine(1) : 1          合计3个三角形，从DocumentTask来看，3层递归。分别是A1-A11-A12,A2-A21-A22,A-A1-A2
//	#0 DocumentTask.processLine(1) : 1          
//	#1 DocumentTask.processLine(1) : 1
//	#2 DocumentTask.processLine(1) : 1
//	End DocumentTask.processLine(2): 3
//	#1 DocumentTask.processLine(1) : 2
//	#2 DocumentTask.processLine(1) : 0
//	End DocumentTask.processLine(2): 3
}

class LineTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = -8047758366201604791L;
	private int start;
	private int end;
	private String word;
	private String[] line;

	public LineTask(int start, int end, String word, String[] line) {
		this.line = line;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute() {
		Integer result = 0;
		if (end - start < 6) {
			result = count(start, end, word, line);
		} else {
			int middle = (end + start) / 2;
			LineTask lt1 = new LineTask(start, middle, word, line);
			LineTask lt2 = new LineTask(middle, end, word, line);
			invokeAll(lt1, lt2);// 因为此处会递归的执行，那么下面result的计算也是递归的
			try {
				Integer result1 = lt1.get();
				System.out.println("LineTask1.compute(1). " + result1);
				Integer result2 = lt2.get();
				System.out.println("LineTask2.compute(2): " + result2);
				result = group(result1, result2);// 等待完成后才能获得,
				System.out.println("End LineTask.compute(3): " + result);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private int group(Integer result1, Integer result2) {
		System.out.println("LineTask Result: " + (result1 + result2));
		return result1 + result2;
	}

	public Integer count(int start, int end, String word, String[] line) {
		int counter = 0;
		for (int i = start; i < end; i++) {
			if (line[i].equals(word)) {
				counter++;
			}
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return counter;
	}

}