package com.zonkware.pi4led.controller;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LedController {

    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalInput myButton1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
    private final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
    private final GpioPinDigitalInput myButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
    private final GpioPinDigitalOutput led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED", PinState.LOW);

    public LedController() {
        initialize();
    }

    public void initialize(){
        myButton1.setShutdownOptions(true);
        led1.setShutdownOptions(true, PinState.LOW);
        myButton2.setShutdownOptions(true);
        led2.setShutdownOptions(true, PinState.LOW);
        myButton1.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                led1.toggle();
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }
        });
        myButton2.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                try {
                    onOff(led1, led2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onOff(GpioPinDigitalOutput l1, GpioPinDigitalOutput l2) throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 8; x++) {
                l1.toggle();
                Thread.sleep(400);
            }
            for (int y = 0; y < 8; y++) {
                l2.toggle();
                Thread.sleep(400);
            }
        }
    }

    @RequestMapping("/")
    public String greeting() {
        return "Hello world!";
    }

    @RequestMapping("/light")
    public String light() {
        led1.toggle();
        return "OK";
    }

}
