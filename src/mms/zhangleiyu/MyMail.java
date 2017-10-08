package mms.zhangleiyu;

import javax.mail.Message;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;  
import java.util.Properties;  
  
public class MyMail {  
    public static void sendMail(String[] toMail, String mailTitle, String mailContent) throws Exception {
    	//从配置文件中加载信息
        Properties props = new Properties(); 
        InputStream in = props.getClass().getResourceAsStream("/config/MMSconfig");
        BufferedReader bf = new BufferedReader(new InputStreamReader(in)); //解决中文乱码问题
        try {
     	   props.load(bf);
     	  } catch (IOException e) {
     	   e.printStackTrace();
     	  }
        String host=props.getProperty("host");
        String from=props.getProperty("from");
        String user=props.getProperty("user");
        String password=props.getProperty("password");
        String fromName=props.getProperty("fromName");
        //配置smtp信息
        props.setProperty("mail.debug", "true");// 开启debug调试
        props.setProperty("mail.smtp.host", host);//邮件服务器
        props.setProperty("mail.smtp.auth", "true");//同时通过验证  
        props.setProperty("mail.transport.protocol", "smtp");//邮件协议名称
        props.setProperty("mail.smtp.socketFactory.class",
        	    "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
  
        Session session = Session.getInstance(props);//根据属性新建一个邮件会话  
  
        MimeMessage message = new MimeMessage(session);//由邮件会话新建一个消息对象  
        message.setFrom(new InternetAddress(from, fromName));//设置发件人的地址  
        //收件人信息处理
        InternetAddress[] toList = new InternetAddress[toMail. length ];  
        for ( int len=0;len<toMail.length;len++){toList[len] = new InternetAddress(toMail[len]);}
        message.setRecipients(Message.RecipientType.TO, toList);//设置收件人,并设置其接收类型为TO  
        message.setSubject(mailTitle);//设置标题  
        //设置信件内容  
        message.setContent(mailContent, "text/html;charset=gbk"); //发送HTML邮件，内容样式比较丰富  
        message.setSentDate(new Date());//设置发信时间  
        message.saveChanges();//存储邮件信息  
  
        //发送邮件  
        Transport transport = session.getTransport();  
        transport.connect(user, password);  
        transport.sendMessage(message, message.getAllRecipients());//发送邮件,其中第二个参数是所有已设好的收件人地址  
        transport.close();  
    }  
    /*public static void main(String[] args) throws Exception {  
    	String[] to={"812555149@qq.com"};
        sendMail( to, "会议管理系统测试邮件", "<b>这里是邮件正文</b>");
    }  */
}  
