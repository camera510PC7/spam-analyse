package src.main.java;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;
import javax.swing.*;
import java.awt.*;


public class Main{
    private MainView mainView;
    private Imap imap;
    public static void main(String[] args){
        Main main = new Main("SPAM-ANALYSE");
    }

    Main(String title){
        mainView = new MainView(title,500,500);
        Set<String> urlList = new HashSet<String>();
        imap = new Imap();
        urlList = imap.getUrlList();
        mainView.setList(urlList);
        //System.out.println(urlList);
        for(String url:urlList){
            System.out.println(url);
        }
    }
}

class MainView {
    private JFrame frame;
    private JPanel panel;
    private JList<String> list;
    private DefaultListModel<String> model;
    private JLabel titleLabel;

    public MainView(String title,int width,int heigh){
        frame = new JFrame(title);
        panel = new JPanel();      
        frame.setSize(width,heigh);

        list = new JList<String>();
        model = new DefaultListModel<String>();

        list.setModel(model);

        JScrollPane sp = new JScrollPane();
        sp.getViewport().setView(list);
        sp.setPreferredSize(new Dimension(400,400));

        titleLabel = new JLabel("URL一覧");
        titleLabel.setPreferredSize(new Dimension(50,50));
        panel.add(titleLabel);
        panel.add(sp);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setList(Set<String> urlList){
        for(String s:urlList){
            model.addElement(s);
        }
        list.ensureIndexIsVisible(0);
    }
}