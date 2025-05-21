package src.GUI;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI {
    public static void start(){
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e){
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        
        MainFrame e = new MainFrame();
    }
}