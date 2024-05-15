package src.main.java;
import java.util.Properties;
import java.util.regex.*;
import java.util.Set;
import java.util.HashSet;
import jakarta.mail.*;

public class Imap{

    public static Set<String> getUrlList(){
        String mailServer = "";
        String mailAddr = "";
        String mailPasswd = "";

        Set<String> urlList = new HashSet<String>();

        try{
            Properties props = System.getProperties();
            Session session = Session.getInstance(props,null);
            Store imap = session.getStore("imaps");

            imap.connect(mailServer,993,mailAddr,mailPasswd);

            Folder[] folderList = imap.getDefaultFolder().list("*");

            for(Folder folder : folderList){
                System.out.println(folder);
            }

            UIDFolder uf = (UIDFolder)folderList[6];
            folderList[6].open(Folder.READ_ONLY);

            Message[] msgs = folderList[6].getMessages();

            for(int i=0 ;i < msgs.length; i++){
                Long messageId = uf.getUID(msgs[i]);

                Message msg = uf.getMessageByUID(messageId);
                Part part = msg;
                String bodyText = "";
                String bodyHtml = "";

                String subject = "";

                if(part.isMimeType("text/plain")){
                    bodyText = part.getContent().toString();
                }
                else if(part.isMimeType("multipart/*")){
                    Multipart mp = (Multipart)part.getContent();
                    bodyText  = mp.getBodyPart(0).getContent().toString();

                    if(mp.getBodyPart(1).isMimeType("text/html")){
                        bodyHtml = mp.getBodyPart(1).getContent().toString();
                    }
                }

                subject = msgs[i].getSubject();
                System.out.println(subject);

                //System.out.println(bodyText);
                //System.out.println(bodyHtml);

                Pattern p = Pattern.compile("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
                Matcher textMatch = p.matcher(bodyText);
                if(textMatch.find()){
                    System.out.println(textMatch.group());
                    urlList.add(textMatch.group());
                }

                Matcher htmlMatch = p.matcher(bodyHtml);
                if(htmlMatch.find()){
                    System.out.println(htmlMatch.group());
                    urlList.add(htmlMatch.group());
                }
            }

            imap.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("end");

        return urlList;
    }
}