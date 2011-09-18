/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machine;

import java.io.File;
import utilities.Microdrive;

/**
 *
 * @author jsanchez
 */
public class Interface1 {
    // Definitions for ZX Interface I Control Port 0xEF
    // For INning
    private static final int CTRL_IN_WRPROT = 0x01;
    private static final int CTRL_IN_SYNC = 0x02;
    private static final int CTRL_IN_GAP = 0x04;
    private static final int CTRL_IN_DTR = 0x08;
    // For OUTing
    private static final int CTRL_OUT_COMMSDATA = 0x01;
    private static final int CTRL_OUT_COMMSCLK = 0x02;
    private static final int CTRL_OUT_RW = 0x04;
    private static final int CTRL_OUT_ERASE = 0x08;
    private static final int CTRL_OUT_CTS = 0x10;
    private static final int CTRL_OUT_WAIT = 0x20;
    
    // Definitions for ZX Interface I RS232/Network Port 0xF7
    // For INning
    private static final int RSN_IN_NET = 0x01;
    private static final int RSN_IN_TXDATA = 0x80;
    // For OUTing
    private static final int RSN_OUT_NET_RXDATA = 0x01;
    
    private int mdrFlipFlop, mdrSelected;
    private byte numMicrodrives;
    private Microdrive microdrive[];
    private boolean commsClk;
    
    public Interface1() {
        mdrFlipFlop = 0;
        mdrSelected = 0;
        numMicrodrives = 8;
        microdrive = new Microdrive[8];
        for (int mdr = 0; mdr < 8; mdr++)
            microdrive[mdr] = new Microdrive();
        
        commsClk = false;
        
        if (!microdrive[0].insert(new File("/home/jsanchez/Spectrum/demo.mdr"))) {
            System.out.println("No se ha podido cargar el cartucho en MDR 1");
        }
        
        if (!microdrive[1].insert(new File("/home/jsanchez/Spectrum/wr-prot.mdr"))) {
            System.out.println("No se ha podido cargar el cartucho en MDR 2");
        }
        
        if (!microdrive[2].insert(new File("/home/jsanchez/Spectrum/formatted.mdr"))) {
            System.out.println("No se ha podido cargar el cartucho en MDR 3");
        }
    }
    
    public int readControlPort() {
        if (mdrFlipFlop != 0 && microdrive[mdrSelected].isCartridge()) {
//            System.out.println(String.format("readControlPort: Unit %d selected",
//                mdrSelected));
            return microdrive[mdrSelected].readStatus();
        } else {
            return 0xff;
        }
    }
    
    public int readDataPort() {
        if (mdrFlipFlop != 0 && microdrive[mdrSelected].isCartridge()) {
//            System.out.println(String.format("readDataPort: Unit %d selected",
//                mdrSelected));
            return microdrive[mdrSelected].readData();
        } else {
            return 0xff;
        }
    }
    
    public void writeControlPort(int value) {
        if ((value & CTRL_OUT_COMMSCLK) == 0 && commsClk) {
            mdrFlipFlop <<= 1;
            if ((value & CTRL_OUT_COMMSDATA) == 0) {
                mdrFlipFlop |= 0x01;
            }
            
            mdrFlipFlop &= 0xff;
            if (mdrFlipFlop != 0) {
                switch (mdrFlipFlop) {
                    case 1:
                        mdrSelected = 0;
                        break;
                    case 2:
                        mdrSelected = 1;
                        break;
                    case 4:
                        mdrSelected = 2;
                        break;
                    case 8:
                        mdrSelected = 3;
                        break;
                    case 16:
                        mdrSelected = 4;
                        break;
                    case 32:
                        mdrSelected = 5;
                        break;
                    case 64:
                        mdrSelected = 6;
                        break;
                    case 128:
                        mdrSelected = 7;
                        break;
                }
//                System.out.println(String.format("MDR %d [%d] selected",
//                    mdrFlipFlop, mdrSelected));
//            } else {
//                System.out.println("All MDR are stopped");
            }
            microdrive[mdrSelected].start();
        }
        
        if (mdrFlipFlop != 0)
            microdrive[mdrSelected].writeControl(value);
        
        commsClk = (value & CTRL_OUT_COMMSCLK) != 0;
//        System.out.println(String.format("erase: %b, r/w: %b",
//            (value & CTRL_OUT_ERASE) != 0, (value & CTRL_OUT_RW) != 0));
    }
    
    public void writeDataPort(int value) {
        if (mdrFlipFlop != 0) {
//            System.out.println(String.format("readDataPort: Unit %d selected",
//                mdrSelected));
            microdrive[mdrSelected].writeData(value);
        }
    }
}