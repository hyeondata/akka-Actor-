package org.example;

/**
 * Hello world!
 *
 */
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class App {
    public static void main(String[] args) {
        // Actor 시스템 생성
        final ActorSystem system = ActorSystem.create("helloakka");

        try {
            // Printer Actor 생성
            ActorRef printerActor = system.actorOf(Printer.props(), "printerActor");

            // Greeter Actor 생성
            ActorRef howdyGreeter = system.actorOf(Greeter.props("Howdy, Akka!", printerActor), "howdyGreeter");

            //Greeter 액터를 로드 밸런싱을 위해 라우터로 생성
            ActorRef greeterRouter = system.actorOf(new RoundRobinPool(5).props(Greeter.props("Howdy, Akka!", printerActor)), "greeterRouter");

            //RoundRobinPool을 이용해 최대 5개까지 동시 실행
            greeterRouter.tell(new Greeter.Greet(), ActorRef.noSender());
            greeterRouter.tell(new Greeter.Greet(), ActorRef.noSender());
            greeterRouter.tell(new Greeter.Greet(), ActorRef.noSender());
            greeterRouter.tell(new Greeter.Greet(), ActorRef.noSender());


            // Greeter Actor에 메시지 보내기 순차적으로 큐에 넣고 차례대로 실행
            howdyGreeter.tell(new Greeter.Greet(), ActorRef.noSender());
            howdyGreeter.tell(new Greeter.Greet(), ActorRef.noSender());
            howdyGreeter.tell(new Greeter.Greet(), ActorRef.noSender());
            howdyGreeter.tell(new Greeter.Greet(), ActorRef.noSender());

            // 몇 초 후 시스템 종료
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            system.terminate();
        }
    }
}