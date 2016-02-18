#进程与内存
##Android
Android has a tool called procrank (/system/xbin/procrank), which lists out the memory usage of Linux processes in order from highest to lowest usage. The sizes reported per process are VSS, RSS, PSS, and USS.

- VSS：Virtual Set Size.它是一个进程能访问的所有内存空间地址的大小。这个大小包含了一些没有驻留在RAM中的内存，就像mallocs已经被分配但还没有写入。VSS很少用来测量程序的实际使用内存。
- RSS：Resident Set Size.RSS是一个进程在RAM中实际持有的内存大小。RSS可能会产生误导，因为它包含了所有该进程使用的共享库所占用的内存，一个被加载到内存中的共享库可能有很多进程会使用它。RSS不是单个进程使用内存量的精确表示。
- PSS：Proportional Set Size。它与RSS不同，它会按比例分配共享库所占用的内存。例如，如果有三个进程共享一个占30页内存控件的共享库，每个进程在计算PSS的时候，只会计算10页。PSS是一个非常有用的数值，如果系统中所有的进程的PSS相加，所得和即为系统占用内存的总和。当一个进程被杀死后，它所占用的共享库内存将会被其他仍然使用该共享库的进程所分担。在这种方式下，PSS也会带来误导，因为当一个进程被杀后，PSS并不代表系统回收的内存大小。This is a measurement of your app’s RAM use that takes into account sharing pages across processes. Any RAM pages that are unique to your process directly contribute to its PSS value, while pages that are shared with other processes contribute to the PSS value only in proportion to the amount of sharing. For example, a page that is shared between two processes will contribute half of its size to the PSS of each process.
- USS：Unique Set Size，进程独自占用的物理内存。这部分内存完全是该进程独享的。USS是一个非常有用的数值，因为它表明了运行一个特定进程所需的真正内存成本。当一个进程被杀死，USS就是所有系统回收的内存。USS是用来检查进程中是否有内存泄露的最好选择。
- Private (Clean and Dirty) RAM
This is memory that is being used by only your process. This is the bulk of the RAM that the system can reclaim when your app’s process is destroyed. Generally, the most important portion of this is “private dirty” RAM, which is the most expensive because it is used by only your process and its contents exist only in RAM so can’t be paged to storage (because Android does not use swap). All Dalvik and native heap allocations you make will be private dirty RAM; Dalvik and native allocations you share with the Zygote process are shared dirty RAM.

top  | grep app名称 ：查询VSS RSS内存占用信息。可以实时的查询。
ps  |  grep app名称 ：查询VSS RSS内存占用信息。ps和top的功能类似，但是不可以实时查询。
procrank | grep app名称：可以查询到上述4个信息。但是只能查询dalvik进程占用内存。
dumpsys meminfo app名称：可以查询到上述4个信息。可以查出native和dalvik分别占用多少内存
dumpsys [Option]
               meminfo 显示内存信息
               cpuinfo 显示CPU信息
               account 显示accounts信息
               activity 显示所有的activities的信息
               window 显示键盘，窗口和它们的关系
               wifi 显示wifi信息 
1.Pss/Shared Dirty/Private Dirty三列是读取了/proc/process-id/smaps文件获取的。它会对每个虚拟内存块进行解析，然后生成数据。The Private Dirty number is the actual RAM committed to only your app’s heap，Private Dirty它是指非共享的，又不能换页出去（can not be paged to disk ）的内存的大小。比如Linux为了提高分配内存速度而缓冲的小对象，即使你的进程结束，该内存也不会释放掉，它只是又重新回到缓冲中而已。
2. Native Heap Size/Alloc/Free三列是使用C函数mallinfo得到的。
3. Dalvik Heap Size/Alloc/Free并非该cpp文件产生，而是android的Debug类生成。
4. proc/meninfo：机器的内存使用信息
5. /proc/pid/maps pid为进程号，显示当前进程所占用的虚拟地址
6. /proc/pid/statm 进程所占用的内存 
##进程与内存
![进程的地址空间](https://github.com/johngerwang/myrepository/raw/master/pic/process_memory_allocation)			进程的地址空间
	
- Stack空间（进栈和出栈）由操作系统控制，其中主要存储函数地址、函数参数、局部变量等等，所以Stack空间不需要很大，一般为几MB大小。
- Heap空间的使用由程序员控制，程序员可以使用malloc、new、free、delete等函数调用来操作这片地址空间。Heap为程序完成各种复杂任务提供内存空间，所以空间比较大，一般为几百MB到几GB。正是因为Heap空间由程序员管理，所以容易出现使用不当导致严重问题。
- 进程的内存空间只是虚拟内存（或者叫作逻辑内存），而程序的运行需要的是实实在在的内存，即物理内存（RAM）。在必要时，操作系统会将程序运行中申请的内存（虚拟内存）映射到RAM，让进程能够使用物理内存。RAM作为进程运行不可或缺的资源，对系统性能和稳定性有着决定性影响。另外，RAM的一部分被操作系统留作他用，比如显存等等，内存映射和显存等都是由操作系统控制，我们也不必过多地关注它，进程所操作的空间都是虚拟地址空间，无法直接操作RAM。如下图

![进程地址空间与RAM的关系](https://github.com/johngerwang/myrepository/raw/master/pic/process_ram)进程地址空间与RAM的关系

##Android的 java程序为什么容易出现OOM
这个是因为Android系统对dalvik的vm heapsize作了硬性限制，当java进程申请的java空间超过阈值时，就会抛出OOM异常（这个阈值可以是48M、24M、16M等，视机型而定），可以通过adb shell getprop | grep dalvik.vm.heapgrowthlimit查看此值。也就是说，程序发生OMM并不表示RAM不足，而是因为程序申请的java heap对象超过了dalvik vm heapgrowthlimit。也就是说，在RAM充足的情况下，也可能发生OOM。那么Android如何应对RAM的不足呢？java程序发生OMM并不是表示RAM不足，如果RAM真的不足，会发生什么呢？这时Android的memory killer会起作用，当RAM所剩不多时，memory killer会杀死一些优先级比较低的进程来释放物理内存，让高优先级程序得到更多的内存。
这样的设计似乎有些不合理，但是Google为什么这样做呢？这样设计的目的是为了让Android系统能同时让比较多的进程常驻内存，这样程序启动时就不用每次都重新加载到内存，能够给用户更快的响应。迫使每个应用程序使用较小的内存，移动设备非常有限的RAM就能使比较多的app常驻其中。但是有一些大型应用程序是无法忍受vm heapgrowthlimit的限制的.推荐使用jni在native heap上申请空间，nativeheap的增长并不受dalvik vm heapsize的限制，从图6可以看出这一点，它的native heap size已经远远超过了dalvik heap size的限制。只要RAM有剩余空间，程序员可以一直在native heap上申请空间，当然如果 RAM快耗尽，memory killer会杀进程释放RAM。大家使用一些软件时，有时候会闪退，就可能是软件在native层申请了比较多的内存导致的。比如，我就碰到过UC web在浏览内容比较多的网页时闪退，原因就是其native heap增长到比较大的值，占用了大量的RAM，被memory killer杀掉了。

- 通过adb shell cat /proc/meminfo可以查看RAM使用情况。对该命令产生的一些字段的解释。MemTotal：可以使用的RAM总和（小于实际RAM，操作系统预留了一部分）。MemFree：未使用的RAM。Cached：缓存（这个也是app可以申请到的内存）
- 注意点。new或者malloc时申请的是虚拟内存，申请后不会立刻映射到屋里内存，即不会占用ram，只有调用memset使用内存后虚拟内存才会真正映射到ram。
- bitmap

##Shallow heap & Retained heap
所有包含Heap Profling功能的工具（MAT, Yourkit, JProfiler, TPTP等）都会使用到两个名词，一个是Shallow Size，另一个是 Retained Size.

- Shallow Size
对象自身占用的内存大小，不包括它引用的对象。
针对非数组类型的对象，它的大小就是对象与它所有的成员变量大小的总和。当然这里面还会包括一些java语言特性的数据存储单元。
针对数组类型的对象，它的大小是数组元素对象的大小总和。
- Retained Size
Retained Size=当前对象大小（Shallow Size)+当前对象可直接或间接引用到的对象的大小总和。(间接引用的含义：A->B->C, C就是间接引用)
换句话说，Retained Size就是当前对象被GC后，从Heap上总共能释放掉的内存。
不过，释放的时候还要排除被GC Roots直接或间接引用的对象,即被GC Roots和该对象所共享的直接或者间接引用的对象要排除。他们暂时不会被被当做Garbage。

##MAT(Memory Analyze Tool)内存分析工具
- MAT无法解析通过DDMS获得的hprof文件的格式，所以需要通过android的hprof-conv命令来转换一下。
hprof-conv scr.hprof dst.hprof
- list objects -- with outgoing references : 查看这个对象持有的外部对象引用。OBJECT
- list objects -- with incoming references : 查看这个对象被哪些外部对象引用。OBJECT
- show objects by class  --  with outgoing references ：查看这个对象类型持有的外部对象引用。CLASS
- show objects by class  --  with incoming references ：查看这个对象类型被哪些外部对象引用。CLASS

##查看各进程所占内存情况
system/xbin/showmap push到手机 system/xbin 目录，并且修改权限为可执行
adb push ./system/xbin/showmap system/xbin
adb chmod 777 system/xbin/showmap
adb shell showmap 8179（进程号）
注意：需要eng版本。


##获取进程使用内存的dump
- adb shell am dumpheap 8179 /storage/sdcard0/08152110.hprof
pid：8179。存储路径：/storage/sdcard0/08152110.hprof
1. 该命令输出的每行表示一个vm area，列出了该vm area的start addr, end addr, Vss, Rss, Pss, shared clean, shared dirty, private clean, private dirty，object。 
2. start addr和end addr表示进程空间的起止虚拟地址
3. Object可以看做mmap的文件名

##查看系统内存情况
adb shell cat /proc/meminfo
MemTotal：可以使用的RAM总和（小于实际RAM，操作系统预留了一部分）
MemFree：未使用的RAM
Cached：缓存（这个也是app可以申请到的内存）
Buffers：缓存（这个也是app可以申请到的内存）
空闲内存=MemFree+Buffers+Cached 

##GC Roots
There are four kinds of GC roots in Java:

- Local variables are kept alive by the stack of a thread. This is not a real object virtual reference and thus is not visible. For all intents and purposes, local variables are GC roots.
- Active Java threads are always considered live objects and are therefore GC roots. This is especially important for thread local variables.
- Static variables are referenced by their classes. This fact makes them de facto GC roots. Classes themselves can be garbage-collected, which would remove all referenced static variables. This is of special importance when we use application servers, OSGi containers or class loaders in general. We will discuss the related problems in the Problem Patterns section.
- JNI References are Java objects that the native code has created as part of a JNI call. Objects thus created are treated specially because the JVM does not know if it is being referenced by the native code or not. Such objects represent a very special form of GC root, which we will examine in more detail in the Problem Patterns section below.



