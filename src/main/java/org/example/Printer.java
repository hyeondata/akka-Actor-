package org.example;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Printer extends AbstractActor {
    public static Props props() {
        return Props.create(Printer.class, Printer::new);
    }

    static public class Greeting {
        public final String message;

        public Greeting(String message) {
            this.message = message;

        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeting.class, greeting -> {

                    System.out.println(greeting.message);
                })
                .build();
    }
}