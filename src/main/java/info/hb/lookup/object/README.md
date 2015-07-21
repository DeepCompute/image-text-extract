
## 从图片重提取目标

It is a nice, simple and friendly to use library which helps you to lookup objects on a screen. Also it has a OCR functionality.

Using Lookup you can do Java OCR tricks like recognizing any infromation from your Robot application. Which can be
usefull for debuging or automating things.

Lookup project designed to lookup images in images. It based on NCC algorithm. Turns out it is good for simple OCR purpose as well.

Details on NCC (Normalized cross correlation) can be found in 'doc' folder (a lot of math).

Choose 'lookup' over OpenCL if you need fast simple, pure java solution.

## OCR功能

If you need to encode special symbols use UNICODE in the file name. For example if you need to have '\' character (which is prohibited
in the path and file name) specify %2F.png as a image symbol name.

Sometimes you need specify two different image for one symbol (if image / font symbol varry too mutch). To do so add unicode ZERO WIDTH SPACE symbol to the filename. Like that %2F%E2%80%8B.png will produce '/' symbol as well. 

    package com.github.axet.lookup;
    
    import java.io.File;
    
    import com.github.axet.lookup.common.ImageBinaryGrey;
    
    public class OCRTest {
    
        static public void main(String[] args) {
            OCR l = new OCR(0.70f);
    
            // will go to com/github/axet/lookup/fonts folder and load all font
            // familys (here is only font_1 family in this library)
            l.loadFontsDirectory(OCRTest.class, new File("fonts"));
    
            // example how to load only one family
            // "com/github/axet/lookup/fonts/font_1"
            l.loadFont(OCRTest.class, new File("fonts", "font_1"));
    
            String str = "";
    
            // recognize using all familys set
            str = l.recognize(Capture.load(OCRTest.class, "test3.png"));
            System.out.println(str);
    
            // recognize using only one family set
            str = l.recognize(Capture.load(OCRTest.class, "test3.png"), "font_1");
            System.out.println(str);
    
            // recognize using only one family set and rectangle
            ImageBinaryGrey i = new ImageBinaryGrey(Capture.load(OCRTest.class, "full.png"));
            str = l.recognize(i, 1285, 654, 1343, 677, l.getSymbols("font_1"));
            System.out.println(str);
        }
    }


        
## Lookup方法

    package com.github.axet.lookup;
    
    import java.awt.Point;
    import java.awt.image.BufferedImage;
    import java.util.Collections;
    import java.util.List;
    
    import com.github.axet.lookup.common.GFirst;
    import com.github.axet.lookup.common.GPoint;
    import com.github.axet.lookup.common.ImageBinaryGreyScale;
    
    public class SNCCTest {
    
        public static void main(String[] args) {
            BufferedImage image = Capture.load(OCRTest.class, "desktop.png");
            BufferedImage templateSmall = Capture.load(OCRTest.class, "desktop_feature_small.png");
            BufferedImage templateBig = Capture.load(OCRTest.class, "desktop_feature_big.png");
    
            LookupScale s = new LookupScale(0.2f, 10, 0.65f, 0.95f);
    
            ImageBinaryGreyScale si = new ImageBinaryGreyScale(image);
    
            ImageBinaryGreyScale stBig = new ImageBinaryGreyScale(templateBig);
            ImageBinaryGreyScale stSmall = new ImageBinaryGreyScale(templateSmall);
    
            Long l;
    
            System.out.println("big");
            l = System.currentTimeMillis();
            {
                List<GPoint> pp = s.lookupAll(si, stBig);
    
                Collections.sort(pp, new GFirst());
    
                for (GPoint p : pp) {
                    System.out.println(p);
                }
            }
            System.out.println(System.currentTimeMillis() - l);
    
            System.out.println("small");
            l = System.currentTimeMillis();
            {
                List<GPoint> pp = s.lookupAll(si, stSmall);
    
                Collections.sort(pp, new GFirst());
    
                for (GPoint p : pp) {
                    System.out.println(p);
                }
            }
            System.out.println(System.currentTimeMillis() - l);
    
            System.out.println("big");
            l = System.currentTimeMillis();
            {
                List<GPoint> pp = s.lookupAll(si, stBig);
    
                Collections.sort(pp, new GFirst());
    
                for (GPoint p : pp) {
                    System.out.println(p);
                }
            }
            System.out.println(System.currentTimeMillis() - l);
    
        }
    }
