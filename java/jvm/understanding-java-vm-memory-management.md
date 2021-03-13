# Introduction

## Course Introduction

In this class we're going to take a look at various aspects of garbage collection and memory management in the Java virtual machine, including --- how the GC works, how we can interact with the Garbage Collector, both from an external point of view, i.e. using some of the tools that the Java virtual machine provides for us, and also how we can interact with the Garbage Collector internally using some of the classes within the VM.
이 클래스에서는 Java 가상 머신에서 가비지 수집 및 메모리 관리의 다양한 측면을 살펴볼 것입니다. --- GC 작동 방식, 가비지 콜렉터와 상호 작용하는 방법, 즉, Java 가상 머신이 제공하는 일부 도구 사용 및 VM 내의 일부 클래스를 사용하여 가비지 콜렉터와 내부적으로 상호 작용할 수있는 방법을 사용합니다.

Before we do that I think it's good to understand why we need garbage collection at all, so in languages before Java, such as C and C++, whenever we allocated memory, so in this example where we allocate an object, at some point we need to remember to delete that object and to free up that memory. If we didn't do that we'd end up with a memory leak.
그렇게하기 전에 가비지 수집이 필요한 이유를 이해하는 것이 좋다고 생각합니다. 따라서 메모리를 할당 할 때마다 C 및 C ++와 같은 Java 이전의 언어로,이 예에서는 객체를 할당하는 시점에서 어느 시점에서 해당 객체를 삭제하고 해당 메모리를 비워야합니다. 그렇게하지 않으면 메모리 누수가 발생합니다.

In Java that's not the case, so I Java we can allocate an object, we can use it, and then when that object is no longer being referenced the Garbage Collector will release the memory used by that object.
그렇지 않은 Java에서는 Java를 사용하여 객체를 할당하고 사용할 수 있으며 해당 객체가 더 이상 참조되지 않을 때 가비지 콜렉터는 해당 객체가 사용하는 메모리를 해제합니다.

There are also issues around who should delete an object, so here we call a method called getAccount that returns an Account object.
누가 객체를 삭제해야하는지에 대한 문제도 있으므로 여기서는 Account 객체를 반환하는 getAccount라는 메소드를 호출합니다.

Who actually owns the object? Is it the receiver of the Account object, so am I responsible for deleting it or is it the giver of the object, the thing that implements getAccount? If we're not clear about that and neither side deletes the object we end up with a memory leak and if we're not clear about that and both sides delete the object we'll end up with some null pointer exception later down the line, which will be very very hard to track down.
누가 실제로 object를 소유합니까? 계정 객체의 수신자입니까? 그래서 삭제할 책임이 있습니까? 아니면 getAccount를 구현하는 객체의 제공자입니까? 우리가 그것에 대해 명확하지 않고 어느 쪽도 객체를 삭제하지 않으면 우리는 메모리 누수로 끝납니다. 우리가 그것에 대해 명확하지 않고 양쪽이 객체를 삭제하면 나중에 줄 아래에 null 포인터 예외가 생깁니다. 추적하기가 매우 어렵습니다.

Finally, we can use objects with some confidence, so, so long as I have a reference to an object there's no way the Garbage Collector will free up that memory.
마지막으로, 우리는 약간의 자신감을 가지고 객체를 사용할 수 있으므로 객체에 대한 참조가있는 한 가비지 콜렉터가 해당 메모리를 비울 수있는 방법이 없습니다.

I will always be able to hold onto that object and use it.
나는 항상 그 object를 잡고 사용할 수 있습니다.

In other environments, such as C++, a thread in the background may take a reference object and delete the object.

C ++과 같은 다른 환경에서는 백그라운드의 스레드가 참조 객체를 가져 와서 삭제할 수 있습니다.

When I then come to use it and I now have a pointer that's pointing at nothing and I end up with either memory corruption or again, with a null pointer exception somewhere later in the code, again, something else that's very very hard to track down.
그런 다음 사용하려고하면 이제 아무것도 가리 키지 않는 포인터가 생겼으며 메모리 손상 또는 다시 발생합니다. 코드에서 나중에 어딘가에 null 포인터 예외가 발생하여 추적하기가 매우 어렵습니다.

Garbage Collectors make a promise and that promise is they claim no live objects.
가비지 콜렉터는 약속하며 약속은 실제 오브젝트가 없다고 주장하는 것입니다.

However, Garbage Collectors make no promise about dead objects.
그러나 가비지 콜렉터는 죽은 objects에 대해 약속하지 않습니다.

They may collect them, they may not, and if they do collect them you don't know when they're going to run, i.e. when that memory is going to be reclaimed.
그들은 수집 할 수도 있고 수집하지 않을 수도 있습니다. 수집 할 경우 언제 실행할지, 즉 메모리가 회수 될 시기를 모릅니다.

Here, for example, we allocate an Account object, we do new Account, and we have something that references a block of memory that contains that Account object.
예를 들어, Account 객체를 할당하고, 새로운 Account를 수행하며, 해당 Account 객체를 포함하는 메모리 블록을 참조하는 무언가가 있습니다.

If it's on a later stage we take that same variable and point it at a new account.
이후 단계에 있다면 동일한 변수를 사용하여 새 account 객체를 지정하십시오.

We're now referencing a new piece of memory, the new Account object.
우리는 이제 새로운 메모리, 새로운 Account 객체를 참조하고 있습니다.

The only guarantee that we have, and it's a good guarantee, is that that second Account object will be live forever, so long as we have a reference to it the Garbage Collector won't collect it.
가비지 콜렉터가 이를 수집하지 않는 한 두 번째 계정 오브젝트가 영구적으로 유지된다는 것입니다.

We have no guarantees about the old Account object. It's no longer being referenced and it may be reclaimed, but it may not.
기존 계정 개체에 대해 보증하지 않습니다. 더 이상 참조되지 않고 회수 될 수 있지만 그렇지 않을 수도 있습니다.

Even if we have an environment like Java where the Garbage Collector will run, we don't know when it will run, and we don't know that it'll run before the end of our application.
가비지 콜렉터가 실행될 Java와 같은 환경이 있더라도 언제 실행 될지 알지 못하며 애플리케이션이 끝나기 전에 실행 될지 알 수 없습니다.

## Different Types of Garbage Collection

There are also different types of Garbage Collection.
가비지 콜렉션에는 여러 유형이 있습니다.

One type is do nothing.
한 가지 유형은 아무것도하지 않습니다.

The Garbage Collector might just decide never to run, never to do anything, no memory gets freed, but we do still have the guarantee of not collecting live objects, and that's obviously a good thing.
가비지 콜렉터는 실행하지 말고, 아무 것도하지 말고, 메모리를 확보하지 않기로 결정할 수도 있지만 라이브 오브젝트를 수집하지 않을 것이라는 보장은 여전히 있습니다.

There's also Reference Counting Garbage Collection and a good example of this is the com programming environment.
Reference Counting Garbage Collection도 있으며 이것의 좋은 예는 com 프로그래밍 환경입니다.

In com applications make calls to two functions, AddRef and Release.
com 응용 프로그램에서 AddRef 및 Release의 두 가지 기능을 호출합니다.

AddRef increments a count on an object and Release a decrements account.
AddRef는 개체 수를 늘리고 Release는 account object 수를 줄입니다.

When out count goes to 0 the objects no longer being referenced and the object can now clean itself--- up and remove it all from memory.
카운트가 0에 도달하면 객체는 더 이상 참조되지 않으며 이제 객체를 정리하여 메모리에서 모두 제거 할 수 있습니다.

We have Mark and Sweep Garbage Collectors.
마크와 스윕 가비지 콜렉터가 있습니다.

For the Mark and Sweep Garbage Collector when the Garbage Collector runs it runs in two phases.
가비지 콜렉터가 실행될 때 마크 및 스윕 가비지 콜렉터의 경우 두 단계로 실행됩니다.

The Mark phase walks through all live memory marking that memory as still being alive and the Sweep phase removes all unused memory and this leaves us with memory that could be fragmented.
Mark 단계는 메모리가 여전히 활성 상태임을 표시하는 모든 라이브 메모리 표시를 안내하며 Sweep 단계는 사용되지 않은 모든 메모리를 제거하므로 조각난 메모리가 남습니다.

We have a Copying Garbage Collector and typically with a Copying Garbage Collector this'll work hand in hand with something like a Mark and Sweep collector.
Copying Garbage Collector가 있으며 일반적으로 Copying Garbage Collector를 사용하면 Mark 및 Sweep 수집기와 같은 방식으로 작동합니다.

In this case, after the Sweep phase, all the memory that's left is copied from one buffer to another, and at the same time once the memory's been copied we'll rearrange it, so that it's no longer fragmented.
이 경우 스윕 단계 후에 남은 모든 메모리가 한 버퍼에서 다른 버퍼로 복사되며 동시에 메모리가 복사되면 다시 정렬하여 더 이상 조각화되지 않습니다.

We also have Generational Garbage Collectors.
또한 세대 별 가비지 콜렉터도 있습니다.

The idea behind a Generational Garbage Collector is that if an object survives a Garbage Collect it's likely to be an object that's going to be around for a long time.
세대 별 가비지 콜렉터의 기본 개념은 오브젝트가 가비지 콜렉션에 남아 있으면 오랫동안 주변에있을 오브젝트 일 가능성이 있다는 것입니다.

In that case, once an object survives one Garbage Collect, the Garbage Collector may not look at it again, for a while, and this improves the performance of the Garbage Collector, and finally, there's Incremental Garbage Collectors, and in fact, the Generational Garbage Collector is a form of Incremental garbage collection, so an Incremental Garbage Collector is a Garbage Collector that doesn't look at all the memory all the time during a Garbage Collect.
이 경우 가비지 콜렉터가 하나의 가비지 콜렉터에서 살아남은 후에는 가비지 콜렉터가 잠시 동안 이를 다시 보지 않을 수 있으며, 이는 가비지 콜렉터의 성능을 향상시키고 마지막으로 증분 가비지 콜렉터가 있습니다. 가비지 콜렉터는 증분 가비지 콜렉션의 한 형태이므로 가비지 콜렉터는 가비지 콜렉터 동안 항상 모든 메모리를 보지 않는 가비지 콜렉터입니다.

At last we can see from this, Garbage Collectors tend not to be just of one type, so we tend to have a mixture of Mark and Sweep, Copying, Generational within a single Garbage Collector.
마지막으로 가비지 콜렉터는 한 가지 유형이 아닌 경향이 있으므로 단일 가비지 콜렉터 내에 Mark와 Sweep, Copying, Generational이 혼합되어 있습니다.

## Reference Counted Garbage Collection

I wanted to take a brief look at each of these forms of Garbage Collection in turn.
가비지 콜렉션의 각 양식을 차례로 살펴보고 싶었습니다.

Here we are showing the idea behind a Reference Counted Garbage Collector, so we have two objects here.
여기에서는 Reference Counted Garbage Collector의 개념을 보여 주므로 여기에 두 개의 객체가 있습니다.

The object on the left has a reference count of 2, and the object on the right is a reference count of 1.
왼쪽에있는 개체의 참조 카운트는 2이고 오른쪽에있는 개체의 참조 카운트는 1입니다.

These objects are referencing each other, and that raises each's reference count.
이 객체들은 서로를 참조하고 있으며, 이는 각각의 참조 횟수를 증가시킵니다.

This is called a circular reference and this is one of the issues with the Reference Counting Garbage Collector.
이것을 순환 참조라고하며 이것은 참조 카운트 가비지 콜렉터의 문제점 중 하나입니다.

It's very hard to get rid of circle references, so for example, once the reference on the left goes away the count of both objects is now 1, although both of these objects actually garbage and there's no external reference to them, and this can be a very difficult problem to solve.
circle references를 제거하는 것은 매우 어렵습니다. 예를 들어, 왼쪽의 참조가 사라지면 두 객체의 개수는 이제 1이지만,이 두 객체는 실제로 가비지이고 외부 참조는 없습니다. 해결하기 매우 어려운 문제입니다.

The way Reference Counting works is that a given object, once it's been assigned, will have its Reference Count incremented, and when it's be de-referenced and de-assignment, it'll have the reference count decremented.
참조 카운트가 작동하는 방식은 지정된 객체가 일단 할당되면 참조 카운트가 증가하고 참조 해제 및 할당 해제시 참조 카운트가 감소하는 것입니다.

When its reference count goes to 0 out in the first object here, the object is then freed.
여기서 첫 번째 오브젝트에서 참조 카운트가 0이되면 오브젝트가 해제됩니다.

While the reference count is non-zero, then the object doesn't get freed, and I'll see there are issues with this, people might forget to call AddRef for example, i.e. to increment the reference count.
참조 카운트가 0이 아닌 동안 객체가 해제되지 않으며 이에 문제가 있음을 알 수 있습니다. 예를 들어 참조 카운트를 증가시키기 위해 AddRef를 호출하는 것을 잊어 버릴 수 있습니다.

They might forget to deref the object and decrement the reference count, so we can end up with memory leaks.
객체를 참조 취소하고 참조 횟수를 줄이는 것을 잊어 버려서 메모리 누수로 끝날 수 있습니다.

We could also end up with scenarios where somebody decrements the reference count, but the object is still in use, so not the ideal situation.
누군가가 참조 횟수를 줄인 시나리오가 생길 수 있지만 객체가 여전히 사용 중이므로 이상적인 상황은 아닙니다.

## Mark and Sweep Garbage Collection

Mark and Sweep Garbage Collectors actually typically have three phases, a mark phase that identifies the objects that are still in use, a sweep phase that removes unused objects, and then a compact phase to compact the memory after all unused objects have been removed.
마크 및 스윕 가비지 콜렉터에는 일반적으로 3 단계, 여전히 사용중인 오브젝트를 식별하는 마크 단계, 사용되지 않은 오브젝트를 제거하는 스윕 단계 및 사용되지 않은 모든 오브젝트가 제거 된 후 메모리를 압축하는 컴팩트 단계가 있습니다.

Here we have a simple diagram of a block of memory.
여기에 메모리 블록의 간단한 다이어그램이 있습니다.

On the left we have a Root set of references, so these are references that we can follow from some root mechanism, maybe the stack, the point at live memory.
왼쪽에는 루트 참조 세트가 있으므로 이는 루트 메모리, 스택, 라이브 메모리의 포인트 등 루트 메커니즘에서 따를 수있는 참조입니다.

If we follow the references from the Root set any object that references another object also keeps that object alive, and in the Mark phase the Garbage Collector will start at the Root set and work through each object in turn following all of its references, marking each object that's still alive in memory.
루트 세트의 참조를 따르는 경우 다른 오브젝트를 참조하는 오브젝트도 해당 오브젝트를 유지하며 마크 단계에서 가비지 콜렉터는 루트 세트에서 시작하여 각 오브젝트를 차례로 차례로 참조하여 각 오브젝트를 표시합니다. 메모리에 여전히 존재하는 객체.

Notice that we could have cycles here and those cycles don't affect the Garbage Collector, so if one object references another, but there's no root reference to them, that memory can be collected.
여기에 주기가 있을 수 있으며 이러한 주기는 가비지 콜렉터에 영향을 미치지 않으므로 한 오브젝트가 다른 오브젝트를 참조하지만 해당 오브젝트에 대한 루트 참조가 없는 경우 해당 메모리를 수집 할 수 있습니다.

In the sweep phase the garbage is taken away and that leaves all the objects still in memory still being referenced, and then finally this memory is compacted, so at this point we've changed the physical addresses of the memory, and this is one important point to make about Garbage Collectors.
스윕 단계에서 가비지가 제거되어 메모리에있는 모든 오브젝트가 여전히 참조되는 상태로 남은 다음이 메모리가 압축되므로이 시점에서 메모리의 실제 주소가 변경되었으며 이는 중요한 것 중 하나입니다. 가비지 콜렉터에 대해 작성하십시오.

In applications such as Java we tend not to have physical references to objects.
Java와 같은 애플리케이션에서는 객체에 대한 물리적 참조가없는 경향이 있습니다.

We'll have some reference in our application, which the Java virtual machine internally will be able to use to get an actual physical location for that piece of memory, but it's very hard for us to actually get at that physical location.
애플리케이션에서 Java 가상 머신이 내부적으로 해당 메모리 조각의 실제 물리적 위치를 얻는 데 사용할 수있는 참조가 있지만 실제로 해당 물리적 위치를 얻는 것은 매우 어렵습니다.

The copying Garbage Collector things are slightly different.
copying GC는 약간 다릅니다.

There's still typically a mark phase and a sweep phase maybe, so here for example, we have a block of memory.
일반적으로 마크 단계와 스윕 단계가 여전히있을 수 있으므로 여기에 예를 들어 메모리 블록이 있습니다.

This was allocated on the left and this is where all of our current objects are being allocated.
이것은 왼쪽에 할당되었으며 현재 모든 객체가 할당되는 곳입니다.

The way Garbage Collector will work typically is when this block of memory called the fromspace becomes full the Garbage Collector runs.
가비지 콜렉터가 일반적으로 작동하는 방식은 fromspace라고 하는 이 메모리 블록이 가비지 콜렉터가 실행되는 시점입니다.

Again, it follows the Root set and from the Root set marks all the live objects.
다시, 루트 세트를 따르고 루트 세트에서 모든 라이브 오브젝트를 표시합니다.

Now what happens during the sweep phase is these objects are moved into the tospace, and so compacted at the same time.
스윕 단계에서 발생하는 일은 이러한 오브젝트가 tospace으로 이동하여 동시에 압축됩니다.

Finally, the fromspace is cleared.
마지막으로 fromspace가 지워집니다.

The tospace and the fromspace are now swapped.
Tospace와 Fromspace가 이제 교체되었습니다.

The next piece of memory to get allocated gets allocated in this new fromspace and then finally when that becomes full the whole process happens again.
할당 할 다음 메모리 조각은 이 새로운 fromspace에서 할당 된 다음 마지막으로 가득 차면 전체 프로세스가 다시 발생합니다.

## Generational Garbage Collection

We also have the idea of Generational Garbage Collectors.
우리는 세대 별 가비지 콜렉터에 대한 아이디어도 가지고 있습니다.

The idea here is that once an object survives a garbage collection that object is promoted to a different generation. 
여기서 아이디어는 객체가 가비지 콜렉션에서 살아남으면 해당 객체가 다른 세대로 승격된다는 것입니다.

The Garbage Collector will sweep through the young generations more often than it sweeps through the older generations, and depending on the environment there could be any number of different generations, so in Java for example, there's two, in. NET there might be then a three.
가비지 콜렉터는 이전 세대를 스위프하는 것보다 젊은 세대를 더 자주 스윕합니다. 환경에 따라 여러 세대가있을 수 있으므로 Java에는 예를 들어 두 개가 있습니다. NET에는 다음이있을 수 있습니다. 세.

Again, different environments, there might be two or more generations that the Garbage Collector manages to collect memory. With a Generational Garbage Collector again, similar to before, we have a block of memory into which we're allocating our objects, and again, that memory's become full, but this is now in our first generation, our young generation. In that generation we've allocated memory.
다시 말하지만, 다른 환경에서는 가비지 콜렉터가 메모리를 수집하기 위해 관리하는 둘 이상의 세대가있을 수 있습니다. 이전과 마찬가지로 세대 가비지 콜렉터를 사용하여 오브젝트를 할당 할 메모리 블록이 있으며, 그 메모리가 가득 찼지만 이제는 1 세대 젊은 세대입니다. 그 세대에 우리는 메모리를 할당했습니다.

Notice we also have an old generation where there may be objects that may be alive, may not be alive. 
Once the GC runs all the objects, in this case, 1, 2, 3, and 4 that survive a Garbage Collect will be moved to the Old Generation.
살아있을 수도 있고 없을 수도있는 물체가있을 수있는 구세대도 있습니다.
GC가 모든 개체를 실행하면 (이 경우 가비지 수집에서 살아남은 1, 2, 3, 4)는 old 세대로 이동합니다.

We can then clear the Young Generation and then carry on allocating objects inside the Young Generation.
그런 다음 Young Generation을 지우고 Young Generation 내부에 객체를 할당 할 수 있습니다.

## Demonstrating How the Garbage Collection Works

What I'd like to do is to run some code that's going to allocate some memory and then take the address of that memory in Java and print out the address of the memory.
내가하고 싶은 것은 메모리를 할당 한 다음 코드를 실행하여 Java에서 해당 메모리의 주소를 가져 와서 메모리 주소를 인쇄하는 것입니다.

The idea behind this is that as we print these addresses we'll see that the memory address rises as we allocate more and more objects, and then eventually the Garbage Collector will run, and at that point we'll collect all the memory, the next allocation will go back to the same address as the previous allocation, or close to the previous allocation at least.
이 주소 뒤에있는 아이디어는이 주소를 인쇄 할 때 점점 더 많은 객체를 할당 할 때 메모리 주소가 올라가고 결국 가비지 콜렉터가 실행되고이 시점에서 모든 메모리를 수집한다는 것입니다. 다음 할당은 이전 할당과 동일한 주소로 돌아가거나 적어도 이전 할당에 가깝습니다.

We have some code here. 
여기에 코드가 있습니다.

Let me run this first and then I'll take you through the code. 
The application is called Sawtooth. 
It's very simple. 
먼저이 작업을 수행 한 다음 코드를 살펴 보겠습니다.
이 응용 프로그램을 톱니라고합니다.
매우 간단합니다.

We're just going to run this with the class path and if I run this code we'll just see it prints out a large stream of numbers, and at the moment, just by looking at those numbers we can't see too much, but those numbers are the addresses of the objects as they are being allocated. 
우리는 클래스 경로로 이것을 실행하려고합니다.이 코드를 실행하면 큰 숫자 스트림이 인쇄되는 것을 볼 수 있습니다. 현재 그 숫자를 보면 너무 많이 볼 수 없습니다. 그러나 해당 숫자는 할당되는 오브젝트의 주소입니다.

Let me kill that. 
이 어플을 죽여보자.

If I come back into the code I can see how this is going to work. 
We're allocating an object called GCMe and GCMe is defined here. 
GCMe is simply a large object. 
코드로 돌아 오면 이것이 어떻게 작동하는지 알 수 있습니다.
우리는 GCMe라는 객체를 할당하고 여기에 GCMe이 정의되어 있습니다.
GCMe은 단순히 큰 개체입니다.

There's many longs in here, in fact, I had 18 of them, so that makes this quite a large object, and the reason for that is that is that when we allocate this, if it was a small object we need to allocate many many thousands of them before the Garbage Collector kicked in, and if it's larger we need to allocate fewer objects before the Garbage Collector kicks in, so if I look at the code that allocates the objects we see there's a loop. 
여기에는 많은 길이가 있습니다. 실제로, 나는 그들 중 18 개를 가지고 있기 때문에 이것을 상당히 큰 객체로 만들고, 그 이유는 이것을 할당 할 때, 그것이 작은 객체라면 많은 것을 할당해야하기 때문입니다 가비지 콜렉터가 시작되기 전에 수천 개가 있고 더 큰 경우 가비지 콜렉터가 시작되기 전에 더 적은 수의 오브젝트를 할당해야합니다. 따라서 오브젝트를 할당하는 코드를 보면 루프가 있습니다.

Inside the loop we allocate a new object, GCMe. 
루프 안에서 우리는 새로운 객체 GCMe을 할당합니다.

We take the address of the object, we'll see how we do that in a moment, and then simply print out that address.
우리는 객체의 주소를 가져다가 잠시 후에 어떻게하는지 알아 본 다음 간단히 그 주소를 인쇄합니다.

To take the address of the object we have a helper method called addressOf that just gets given an object and this helper uses the Unsafe class. 
객체의 주소를 취하기 위해 우리는 addressOf라는 도우미 메소드를 가지고 있습니다.이 메소드는 단지 객체를 얻었고이 도우미는 Unsafe 클래스를 사용합니다.

Now Unsafe is part of sun.misc.Unsafe. 
이제 안전하지 않은 부분은 sun.misc.safe의 일부입니다.

This is not a standard part of the Java runtime. 
이것은 Java 런타임의 표준 부분이 아닙니다.

It's an undocumented class, but you'll find this used quite often, and it's quite possible that this class will be included officially in the Java 9 runtime. 
문서화되지 않은 클래스이지만이 클래스가 자주 사용되는 것을 알 수 있으며이 클래스가 Java 9 런타임에 공식적으로 포함될 수 있습니다.

There are many websites out there that tell us how to use Unsafe, but essentially we have to get a reference to a singleton class. 
Unsafe를 사용하는 방법을 알려주는 많은 웹 사이트가 있지만 기본적으로 싱글 톤 클래스에 대한 참조를 가져와야합니다.

We can't do that directly as the class is a private constructor and there's also security tracks in place to stop it creating instances of this class. 
클래스는 private 생성자이므로이 클래스의 인스턴스를 만드는 것을 막기위한 보안 트랙도 있습니다.

Once we have a reference to the class we can then use it to get the address of an object and here we're just seeing if the addressSize is 4 or 8, so are we running on the 32 bit environment or 64 bit environment, and then using either getInt to get the address or getLong to get the address and we certainly return that address. 
클래스에 대한 참조가 있으므로 객체의 주소를 가져 오는 데 사용할 수 있습니다. 여기서 addressSize가 4 또는 8인지 확인하면 32 비트 환경 또는 64 비트 환경에서 실행되고 있습니다. 그런 다음 getInt를 사용하여 주소를 얻거나 getLong을 사용하여 주소를 가져오고 해당 주소를 확실히 반환합니다.

That's all the code is doing. 
그게 전부 코드입니다.

If we run this code again, we'll see the same output, okay, not very interesting, but what I'd like to do is to run this code and capture that output to a file and we'll give this file a csv extension, so it'll act like a comma separated variable file, so I'll just call this out.csv. 
이 코드를 다시 실행하면 동일한 출력을 볼 수 있습니다. 흥미롭지는 않지만이 코드를 실행하고 해당 출력을 파일로 캡처하여이 파일에 csv를 제공하는 것이 좋습니다. 확장명이므로 쉼표로 구분 된 변수 파일처럼 작동 하므로이 out.csv를 호출합니다.

Once that file's been created we can check the contents by using cat on out. 
csv and that displays the same data as we saw being printed to the console. 
Again, not very exciting. 
파일이 만들어지면 cat을 사용하여 내용을 확인할 수 있습니다.
csv는 콘솔에 인쇄 된 것과 동일한 데이터를 표시합니다.
다시, 매우 흥미 진진하지 않습니다.

However, what we can do is load that data into Excel. 
그러나 우리가 할 수있는 일은 해당 데이터를 Excel로로드하는 것입니다.

In Excel if we open up out.csv again, all we see is a column of numbers, but now what we can do is if I highlight that column and insert a line graph, so I go to Graphs, Line, and that'll take that data, and thus we can see we get a saw tooth graph and what this is showing is that we start allocating memory at a certain place inside the address space, we keep allocating, allocating, allocating, allocating, eventually we'll try and allocate some memory, we'll have no more space to allocate it, the Garbage Collector will kick in, that will free up the memory, and the next time we allocate memory is at some global location inside the address space, and again, we run through the same process. 
Excel에서 out.csv를 다시 열면 숫자 열만 볼 수 있지만 이제 열을 강조 표시하고 선 그래프를 삽입하면 Graphs, Line으로 이동합니다. 데이터를 가져 와서 톱니 그래프를 얻는 것을 볼 수 있습니다. 이것이 보여주는 것은 주소 공간 내부의 특정 위치에서 메모리 할당을 시작하고 할당, 할당, 할당, 할당을 계속한다는 것입니다. 메모리를 할당하면 더 이상 할당 할 공간이없고 가비지 콜렉터가 시작되어 메모리가 비워지며 다음에 메모리를 할당 할 때 주소 공간 내부의 전역 위치에 있으며 우리는 같은 과정을 겪습니다.

We keep allocating until the Garbage Collector kicks in and then when that happens we run again, the GC runs, and we allocate the next piece of memory at the lowest point inside the address space and off we go again. 
우리는 가비지 콜렉터가 시작될 때까지 계속 할당 한 다음 다시 실행되면 GC가 실행되고 주소 공간 내부의 가장 낮은 지점에 다음 메모리 조각을 할당하고 다시 시작합니다.

Hopefully this illustrates very simply what the GC is doing inside this application. 
바라건대 이것은 GC 가이 응용 프로그램 내에서하는 일을 매우 간단히 보여줍니다.

We'll see later in this course that a far more sophisticated tool is out there for showing the Garbage Collector, but this is a nice simple approach to showing what's happening.
이 과정의 후반에 가비지 콜렉터를 표시하기위한 훨씬 더 정교한 도구가 있음을 알 수 있지만, 이는 진행중인 작업을 표시하는 아주 간단한 방법입니다.