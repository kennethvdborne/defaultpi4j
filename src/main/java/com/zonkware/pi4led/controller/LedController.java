package com.zonkware.pi4led.controller;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LedController {

    private final GpioController gpio = GpioFactory.getInstance();
    // private final GpioPinDigitalInput myButton1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
    // private final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
    // private final GpioPinDigitalInput myButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00);
    // private final GpioPinDigitalInput myButton3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07);
    // private final GpioPinDigitalOutput led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);

    public LedController() {
        initialize();
    }

    public int x = 0;
    public List<GpioPin> list = new ArrayList<GpioPin>();


    public void initialize() {
        for (int i = 0; i < 32; i++) {

            if (i==25) {
                Pin pin = RaspiPin.getPinByAddress(i);
                PinMode mode = PinMode.DIGITAL_INPUT;
                GpioPin button = gpio.provisionPin(pin, mode);
                //button.setShutdownOptions(true);
                button.addListener(new GpioPinListenerDigital() {
                    @Override
                    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                        System.out.println("--- Button pressed ---" + event.getPin());
                        for (GpioPin g: list
                             ) {
                            if (g.getMode().equals(PinMode.DIGITAL_OUTPUT)){
                                GpioPinDigitalOutput gout =(GpioPinDigitalOutput)g;
                                if (gout.getState().isHigh()){
                                    gout.setState(PinState.LOW);
                                }
                                else gout.setState(PinState.HIGH);
                            }

                        }
                    }
                });
                list.add(button);
                x++;
                System.out.println("--- Button created " + button.getPin());
            }

            else {
                Pin pin = RaspiPin.getPinByAddress(i);
                PinMode mode = PinMode.DIGITAL_OUTPUT;
                GpioPin led = gpio.provisionPin(pin, mode);
                //button.setShutdownOptions(true);
                led.addListener(new GpioPinListenerDigital() {
                    @Override
                    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                        System.out.println("--- LED ---" + event.getPin() + " + " + event.getState());
                    }
                });
                list.add(led);
                x++;
                System.out.println("--- LED created " + led.getPin());

            }
        }

/*
    private void startup() throws InterruptedException {
        System.out.println("startupsequence");
        for (int i = 0; i < 3; i++) {
            led2.toggle();
            Thread.sleep(400);
        }
        for (int i = 0; i < 8; i++) {
            led2.toggle();
            Thread.sleep(100);
        }
    }

    public void onOff(GpioPinDigitalOutput l1) throws InterruptedException {
        System.out.println("Looping started");
        for (int i = 0; i < 4; i++) {
            l1.toggle();
            Thread.sleep(100);
        }
        System.out.println("Looping Ended");
    }

    @RequestMapping("/")
    public String greeting() {
        return "Hello world!";
    }

    @RequestMapping("/light")
    public String light() {
        return "OK";
    }
*/
    }
}
