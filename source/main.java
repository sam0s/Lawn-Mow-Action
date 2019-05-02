import javax.swing.*;
import java.awt.event.*;


public class main
{
    public static void main(String[] args){
        
        String[] choices = { "Default", "Custom", "Quit"};
        int ans = 13;
        int ans2 = 5;
        int ans3 = 17;
        int ans4 = 5;
        char ans5 = '☺';
        
        int choice = JOptionPane.showOptionDialog(null,"Choose a game mode", 
                "LAWN MOW ACTION V3", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                choices, 
                choices[0]);
                
        if (choice==1){
        
       
        
        ans = Integer.parseInt( JOptionPane.showInputDialog(null, "Enter enemy frequency (Integer) (Default: 13): ", 
         "LAWN MOW ACTION V3", 1));
         
        ans2 = Integer.parseInt( JOptionPane.showInputDialog(null, "Enter sand frequency (Integer) (Default: 5):", 
         "LAWN MOW ACTION V3", 1));
        
         
        ans3 = Integer.parseInt( JOptionPane.showInputDialog(null, "Enter grass frequency (Integer) (Default: 17):", 
         "LAWN MOW ACTION V3", 1));
         
         ans4 = Integer.parseInt( JOptionPane.showInputDialog(null, "Enter game speed (Integer) (faster< 5 >slower):", 
         "LAWN MOW ACTION V3", 1));
         
         
        ans5 = JOptionPane.showInputDialog(null, "Enter player character (Character) (Default: ☺)", 
         "LAWN MOW ACTION V3", 1).charAt(0); 
        }
        
        if(choice!=2){Game mainGame = new Game(ans,ans2,ans3,ans4,ans5);}
    }
}
