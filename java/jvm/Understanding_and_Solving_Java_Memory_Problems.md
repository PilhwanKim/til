# Course Overview

This course is going to help you solve problems related to memory in Java.
이 과정은 Java의 메모리와 관련된 문제를 해결하는 데 도움이됩니다.

That's really exciting because these are some of the most fun problems to diagnose and solve, and they can also lead to really bad production outages if you don't get them right.
이는 진단하고 해결해야 할 가장 재미있는 문제 중 하나이기 때문에 매우 흥미 롭습니다. 또한 제대로 작동하지 않으면 생산 중단으로 이어질 수도 있습니다.

In this course, we're going to take a scientific approach: measuring what's wrong before we tackle problems.
이 과정에서는 문제를 해결하기 전에 무엇이 잘못되었는지 측정하는 과학적인 접근 방식을 사용합니다.

We'll be solving a variety of different problems, including: memory leaks, where your application constantly eats more and more RAM, over-consuming your memory, and running out of heap, and also seeing how your application can be too slow due to the effect of allocating memory at too fast a rate.
메모리 누수, 응용 프로그램에서 지속적으로 더 많은 RAM을 사용하는 메모리, 과도한 메모리 소비 및 힙 부족, 다음과 같은 이유로 응용 프로그램이 너무 느려질 수있는 방법 확인과 같은 다양한 문제를 해결할 것입니다. 너무 빠른 속도로 메모리를 할당하는 효과.

By the end of this course, you'll know how to solve these problems and also how to understand memory-related problems in general through the use of memory profiling, heap dumps, and object histograms.
이 과정을 마치면 메모리 프로파일 링, 힙 덤프 및 객체 히스토그램을 사용하여 이러한 문제를 해결하는 방법과 일반적으로 메모리 관련 문제를 이해하는 방법을 알게됩니다.

Before beginning the course, you should be familiar with Basic Core Java SE, but that's really all you need to get going.
과정을 시작하기 전에 Basic Core Java SE에 익숙해야하지만 실제로는 모든 과정이 필요합니다.

I hope you'll join me on this journey to learn more about Java performance problems with the Understanding and Solving Memory Problems course, at Pluralsight.
Pluralsight의 메모리 문제 이해 및 해결 과정을 통해 Java 성능 문제에 대해 자세히 알아 보려면이 여정에 동참하길 바랍니다.

# 메모리 문제 : 도전과 해결책

## 메모리 문제 : 도전과 해결책

Hello, and welcome to Understanding and Solving Memory Problems, authored by me, Richard Warburton.
안녕하세요, 리차드 워버튼 (Richard Warburton)이 저술 한 메모리 문제 이해 및 해결에 오신 것을 환영합니다.

Normally when we think about the best way to write Java code, we're often thinking about the maintainability of software.
일반적으로 Java 코드를 작성하는 가장 좋은 방법을 생각할 때 종종 소프트웨어의 유지 관리 가능성에 대해 생각합니다.

For example, does it follow object-oriented programming best practices? Is it well tested? Can you add features easily? This course is a little bit different and in many ways more exciting than that.
예를 들어, 객체 지향 프로그래밍 모범 사례를 준수합니까? 잘 테스트 되었습니까? 기능을 쉽게 추가 할 수 있습니까? 이 과정은 약간 다르며 여러면에서 그보다 흥미 진진합니다.

It talks about the kind of problems that can happen to well-refactored and tested code in production.
프로덕션에서 리팩토링되고 테스트 된 코드에서 발생할 수있는 문제에 대해 설명합니다.

We'll be categorizing and explaining a whole series of problems that are related to memory, and we'll demonstrate how you can use freely available tooling and battle-tested approaches to solve these problems.
우리는 메모리와 관련된 일련의 문제를 분류하고 설명 할 것이며, 자유롭게 사용할 수 있는 툴링과 전투 테스트 방식을 사용하여 이러한 문제를 해결하는 방법을 보여줄 것입니다.

But before we get onto the problems themselves, I think we need to explain why you should care about memory problems at all in your Java application.
그러나 문제 자체를 다루기 전에 Java 응용 프로그램에서 메모리 문제에 대해 왜 신경 써야하는지 설명해야한다고 생각합니다.

After all, isn't it the case that Java is garbage collected, and isn't it the case that well-written code just shouldn't have any of these kind of problems? Well, the first thing to think about when it comes to Java memory problems is that yes, Java does have a garbage collector, or GC, as we may refer to it.
결국, Java가 가비지 수집되는 경우가 아니고 잘 작성된 코드에 이러한 종류의 문제가 없어야합니까? 글쎄, Java 메모리 문제와 관련하여 가장 먼저 생각해야 할 것은 Java에는 가비지 수집기 또는 GC가 있다는 것입니다.

In fact, it was one of the first mainstream and popular program languages to use a GC, although it's far from the first to have ever had one at all.
실제로 GC를 사용하는 최초의 주류이자 인기있는 프로그램 언어 중 하나였습니다.

It's proven to be an incredibly popular feature, and a huge amount of engineering work has gone into making garbage collectors in Java really good, and a lot of work done by GC writers and making them really fast.
엄청나게 인기있는 기능으로 입증되었으며, 엄청난 양의 엔지니어링 작업이 Java에서 가비지 컬렉터를 실제로 훌륭하게 만들고 GC 작가가 많은 작업을 수행하여 실제로 빠르게 만들었습니다.

But the thing to remember is garbage collectors are still just computer programs themselves.
그러나 기억해야 할 것은 가비지 수집기는 여전히 컴퓨터 프로그램 일뿐입니다.

They aren't a kind of magic box that will just solve all your problems when it comes to memory.
메모리와 관련하여 모든 문제를 해결하는 일종의 마술 상자가 아닙니다.

They'll free up memory when it isn't in use by an application, but that's basically the end of their remit.
그들은 응용 프로그램에서 사용하지 않을 때 메모리를 확보하지만 기본적으로 송금이 끝났습니다.

If your code blindly leaks references to memory or references to objects, or if it uses too much memory, then you can still have problems despite the existence of a GC.
코드가 맹목적으로 메모리 또는 객체에 대한 참조를 누출하는 경우 또는 너무 많은 메모리를 사용하는 경우 GC가 존재하더라도 여전히 문제가 발생할 수 있습니다.

This doesn't mean that the GC's broken, it's just that GCs blindly follow your references around in memory.
이것은 GC가 고장났다는 것을 의미하는 것이 아니라, GC가 맹목적으로 당신의 메모리를 참고로 따르는 것입니다.

The code and the objects themselves are still king.
코드와 객체 자체는 여전히 왕입니다.

Now when things go wrong in production, they can often go badly wrong, and sometimes without due warning.
이제 운영 단계에서 문제가 발생하면 심각하게 잘못 될 수 있으며 때로는 적절한 경고없이 진행될 수 있습니다.

Now this is often the case with problems like a memory leak that can result in your application becoming completely unusable to its end users.
이제는 종종 메모리 누수와 같은 문제가 발생하여 최종 사용자가 응용 프로그램을 완전히 사용할 수 없게 될 수 있습니다.

Problems like this can cost businesses a lot of money directly from being unable to process customer transactions, but also can leave really bad reputation issues if they happen often enough.
이와 같은 문제로 인해 기업은 고객 거래를 처리 할 수 없어서 많은 비용이 소요될 수 있지만 자주 발생할 경우 평판이 나빠질 수 있습니다.

In my mind, one of the first things I think of when I hear the word Twitter is still its Fail Whale, but don't worry, this course is here to help, and you won't get any burning tires coming out of your computer screen when watching it, I promise.
내 마음에, 내가 트위터라는 단어를들을 때 가장 먼저 생각하는 것 중 하나는 여전히 실패 고래이지만 걱정하지 마십시오.이 과정은 도움이 될 것이며, 타이어가 타지 않을 것입니다. 그것을 볼 때 컴퓨터 화면, 나는 약속한다.

Now when you come to put out your memory program tire fire, people often take a headless chicken approach to investigation.
지금 당신은 당신의 메모리 프로그램 타이어 불을 끄러 올 때, 사람들은 종종 리서치에 머리없는 닭 처럼 접근을합니다.

That's to say they immediately start looking around in a panicked state to solve the problem.
즉, 문제를 해결하기 위해 당황한 상태에서 즉시 둘러보기 시작합니다.

This may involve just re-reading the code in front of them without using any of the memory-related tooling.
메모리 관련 툴링을 사용하지 않고 코드를 다시 읽는 것이 필요할 수 있습니다.

Perhaps they just guess at what the problem is, or maybe they've seen a memory problem at some point in the past in their career and they just blindly repeat the same solution as fixed the previous problem, on the assumption that this will be the same one.
아마도 그들은 문제가 무엇인지 추측하거나 경력에서 과거의 어느 시점에서 메모리 문제를 보았을 것입니다. 이것은 이전의 문제가 해결 될 것이라는 가정하에 이전 문제를 수정 한 것과 동일한 해결책을 맹목적으로 반복합니다. 같은 것.

This course, on the other hand, advocates a different approach.
반면에 이 과정은 다른 접근 방식을 옹호합니다.

That is to say, a scientific approach.
즉, 과학적 접근 방식입니다.

We aim to give you repeatable solutions and a mental model that helps you think through and solve problems.
우리는 반복 가능한 솔루션과 문제를 생각하고 해결하는 데 도움이되는 mental model을 제공하는 것을 목표로합니다.

Of course, you still need to think through the issues themselves, it's not just algorithmic in nature, it still requires human thought and human interaction.
물론, 당신은 여전히 문제 자체를 통해 생각할 필요가 있습니다. 그것은 단지 알고리즘적인 것이 아니라 여전히 인간의 사고와 인간의 상호 작용이 필요합니다.

But for the type of problem that we'll cover in this course, we'll show you how it manifests itself, go through how you can measure your application to find out what's really wrong, how to find what part of the code base to alter, and then encourage you to re-measure the problem in order to validate that your code- based change has really worked and really solved the problem.
그러나 이 과정에서 다룰 문제의 유형에 대해서는 문제가 어떻게 나타나는지 보여주고, 실제로 무엇이 잘못되었는지 확인하기 위해 응용 프로그램을 측정하는 방법, 코드 기반의 어떤 부분을 찾는 방법을 살펴 봅니다. 코드 기반 변경이 실제로 작동하고 실제로 문제를 해결했는지 확인하기 위해 문제를 다시 측정하도록 권장합니다.

This course covers five key topic areas.
이 과정은 5 가지 주요 주제 영역을 다룹니다.

Since we're being scientific about our problem-solving methodology, we need to be able to measure aspects of the memory in our application.
우리는 문제 해결 방법론에 대해 과학적이기 때문에 응용 프로그램에서 메모리의 측면을 측정 할 수 있어야합니다.

So we start off this course by building up a toolkit of different utilities that let us understand what is consuming the memory and what is allocating memory.
그래서 우리는 메모리를 소비하는 것과 메모리를 할당하는 것이 무엇인지 이해할 수 있도록 다양한 유틸리티의 툴킷을 구축하여이 과정을 시작합니다.

We will cover object histograms, heap dumps, and memory profiling.
객체 히스토그램, 힙 덤프 및 메모리 프로파일 링에 대해 다룰 것입니다.

Now in this course, we only use tools that are freely available for use and development, though in one tools case, you may have to pay money to use it in production, but in general, if you're just dealing with certain development, you can use these tools for free, so you don't have to worry about getting budget approval, a sign-off or anything like that.
이제 이 과정에서는 무료로 사용 및 개발할 수 있는 도구만 사용 하지만 한 도구의 경우 프로덕션 환경에서 사용하기 위해 비용을 지불해야 할 수도 있지만 일반적으로 특정 개발을 처리하는 경우에는 이러한 도구를 무료로 사용할 수 있으므로 예산 승인, sign-off 또는 이와 유사한 사항에 대해 걱정할 필요가 없습니다.

Then, we'll talk about the most basic and common types of memory leaks.
그런 다음 가장 기본적이고 일반적인 유형의 메모리 누수에 대해 이야기하겠습니다.

These are situations where you'll eventually get an out of memory error in your Java application, no matter how large a server and how much memory you have.
서버 크기와 메모리 용량에 관계없이 Java 애플리케이션에서 결국 메모리 부족 오류가 발생하는 상황입니다.

Even though Java has a garbage collector, it's still possible to have memory leaks if you hold onto object references for long enough.
Java에 GC가 있지만 개체 참조를 오랫동안 오래 유지하면 메모리 누수가 발생할 수 있습니다.

If you don't understand how this is possible, or even what an object reference is, then don't worry, it'll all become revealed in this module, and we'll explain things from the first principles.
이것이 어떻게 가능한지 또는 객체 참조가 무엇인지 이해하지 못한다면 걱정하지 마십시오.이 모듈에서 모두 공개되며 첫 번째 원칙의 내용을 설명합니다.

Next, we'll look at the more advanced kinds of memory leaks.
다음으로, 고급 메모리 누수에 대해 살펴 보겠습니다.

Now certain types of memory leak in Java applications tend to be a little bit more tricky than regular Java objects.
이제 Java 애플리케이션에서 특정 유형의 메모리 누수가 일반 Java 오브젝트보다 약간 까다로운 경향이 있습니다.

So for example, memory leaks involving class loaders.
예를 들어 클래스 로더와 관련된 메모리 누수.

It's also possible for Java applications to hold references to off heap buffers, which can cause complications when diagnosing memory leaks.
Java 응용 프로그램이 오프 힙 버퍼에 대한 참조를 보유 할 수도 있으며, 이는 메모리 누수를 진단 할 때 복잡성을 야기 할 수 있습니다.

Finally, we'll also cover memory leaks involving thread local variables, which tend to be a little bit tricky.
마지막으로, 약간 까다로운 경향이있는 스레드 로컬 변수와 관련된 메모리 누수도 다룰 것입니다.

Our fourth topic area is explaining how to solve out of memory errors that aren't memory leak related.
네 번째 주제 영역은 메모리 누수와 관련이없는 메모리 부족 오류를 해결하는 방법을 설명합니다.

So, basically situations where your Java application is limited by the amount of available memory, and really wants to use a lot more memory than that.
따라서 기본적으로 Java 응용 프로그램이 사용 가능한 메모리의 양에 의해 제한되고 실제로 그보다 훨씬 많은 메모리를 사용하려는 상황입니다.

A common meme that you'll hear on the internet is that Java applications tend to use up a lot of memory in general.
인터넷에서 듣게 될 일반적인 밈은 Java 응용 프로그램이 일반적으로 많은 메모리를 사용하는 경향이 있다는 것입니다.

They're described as bloated, or heavyweight, or things like this.
그것들은 부풀어 오르거나 헤비급 또는 이와 유사한 것으로 묘사됩니다.

This can certainly be sometimes true for certain types of application, but it genuinely doesn't have to be the case.
이것은 특정 유형의 응용 프로그램에 대해서는 때때로 사실 일 수 있지만, 반드시 그런 것은 아닙니다.

We'll show you how to find out what is dominating the consumption of your memory, and how to reduce those domination consumers of memory.
우리는 메모리 소비를 지배하는 요소를 찾는 방법과 이러한 지배적 인 메모리 소비자를 줄이는 방법을 보여줍니다.

This also helps you support more customers on your existing hardware infrastructure, and can significantly cut operational costs if you're running in a cloud-based environment.
또한 기존 하드웨어 인프라에서 더 많은 고객을 지원하고 클라우드 기반 환경에서 실행중인 경우 운영 비용을 크게 절감 할 수 있습니다.

Finally, we'll look at and investigate the costs of memory allocation.
마지막으로 메모리 할당 비용을 살펴보고 조사합니다.

Many developers have this idea that calling new in Java is basically free, but in practice, if you allocate a lot of memory, it does take up time and it can slow your application down.
많은 개발자들이 Java에서 새로 new를 부르는 것은 기본적으로 무료라는 사실을 알고 있지만 실제로 많은 메모리를 할당하면 시간이 걸리고 응용 프로그램 속도가 느려질 수 있습니다.

It can also cause increased latency in your application by putting a lot of pressure on certain types of garbage collectors, and increasing garbage collection pauses, and we'll show you how to investigate and solve these problems.
또한 특정 유형의 가비지 수집기에 많은 압력을 가하고 가비지 수집 일시 중지를 증가시켜 응용 프로그램의 대기 시간을 증가시킬 수 있으며 이러한 문제를 조사하고 해결하는 방법을 보여줍니다.
