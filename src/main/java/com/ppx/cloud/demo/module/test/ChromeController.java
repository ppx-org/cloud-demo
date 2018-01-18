package com.ppx.cloud.demo.module.test;

import static io.webfolder.cdp.type.constant.MouseButtonType.Left;
import static io.webfolder.cdp.type.constant.MouseEventType.MousePressed;
import static io.webfolder.cdp.type.constant.MouseEventType.MouseReleased;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.command.Input;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.type.network.Cookie;


@Controller
public class ChromeController {
	
	private static Session chromeSession = null;
	
	private static SessionFactory sessionFactory = null;
	
	public static int DEFAULT_PORT = 9222;
	
	@GetMapping @ResponseBody
	public ModelAndView zhaopin(HttpServletRequest request) {
		
		try {
			System.out.println("----2-1------------------zhaopin begin-------------------");
			
			List<String> arguments = new ArrayList<String>();
			//arguments.add("--headless");
			arguments.add("--window-size=1920,1080");
			arguments.add("--disable-gpu");
			arguments.add("--start-maximized");
			
			
			if (chromeSession == null) {
				
				boolean isPortExist = isHostConnectable("localhost", SessionFactory.DEFAULT_PORT);
				
				Launcher launcher = new Launcher();
				sessionFactory = launcher.launch(arguments);
				
				
				int len = sessionFactory.list().size();
				
				
				if (!isPortExist) {
					chromeSession = sessionFactory.create();
				}
				else if (len >= 1) {
					chromeSession = sessionFactory.connect(sessionFactory.list().get(0).getId());
				}
				else {
					chromeSession = sessionFactory.create();
				}
			}
			else {
				if (!isHostConnectable("localhost", SessionFactory.DEFAULT_PORT)) {
					System.gc();
					Launcher launcher = new Launcher();
					sessionFactory = launcher.launch(arguments);
					chromeSession = sessionFactory.create();
					System.gc();
				}
			}
			
			
			// https://www.baidu.com/ 
			// https://passport.zhaopin.com/org/login
			// file:///C:/Users/LENOVO/Desktop/test.html
			chromeSession.navigate("https://passport.zhaopin.com/org/login");
			chromeSession.waitDocumentReady();
			chromeSession.activate();
		
			chromeSession.wait(300);
			// 会变 CheckCodeCapt CheckCodeCapt
			chromeSession.evaluate("try{$('#checkCodeCapt').click();}catch(){};");
			chromeSession.evaluate("$('#CheckCodeCapt').click();");
			chromeSession.wait(1200);
			
			byte[] data = chromeSession.captureScreenshot();
		    
			String path = "E:/Git/ppx-org/cloud-demo/target/classes/static/test/clip01.png";
		    try {
		    	FileOutputStream out = new FileOutputStream(new File(path));
		    	out.write(data);
		    	out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			System.out.println("----------------------005 end-------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ModelAndView mv = new ModelAndView();
		return mv;
			
	}
	
	
	@RequestMapping @ResponseBody
	public Map<String, Object> click(HttpServletRequest request, String points) {
		double offsetY = 360;
		double offsetX = 535;
		
		offsetX = 630; // 有头 OK
		
		String[] point = points.split(";");
		double x1 = Double.parseDouble(point[0].split(",")[0]) + offsetX;
		double y1 = Double.parseDouble(point[0].split(",")[1]) + offsetY;
		double x2 = Double.parseDouble(point[1].split(",")[0]) + offsetX;
		double y2 = Double.parseDouble(point[1].split(",")[1]) + offsetY;
		double x3 = Double.parseDouble(point[2].split(",")[0]) + offsetX;
		double y3 = Double.parseDouble(point[2].split(",")[1]) + offsetY;
		
		
		click(x1, y1);
		chromeSession.wait(100);
		click(x2, y2);
		chromeSession.wait(110);
		click(x3, y3);
		chromeSession.wait(80);
		
		// 输入用户名和密码
		chromeSession.evaluate("$('#LoginName').val('honghai020');");
		chromeSession.evaluate("$('#Password').val('Test13800');");
		
		chromeSession.evaluate("try{$('#captcha-submitCode').click();}catch(){};");
		chromeSession.evaluate("$('#captcha-submitCode').click();");
		chromeSession.wait(1200);
        
        // 登录 loginbutton loginbutton
		chromeSession.evaluate("$('#loginbutton').click();");  
		chromeSession.wait(3000);
		
		String location = chromeSession.getLocation();
		System.out.println("xxxxlocation:" + location);
		https://rd2.zhaopin.com/s/homepage.asp
		if (location.startsWith("https://rd2.zhaopin.com/s/homepage.asp")) {
			return getCookie(request);
		}
		else {
			return ControllerReturn.ok(0);
		}
        
//       byte[] data = chromeSession.captureScreenshot();
//	    
//	    try {
//	    	FileOutputStream out = new FileOutputStream(new File("E:/U/png/2.png")); 
//	    	out.write(data);
//	    	out.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	@GetMapping @ResponseBody
	private Map<String, Object> getCookie(HttpServletRequest request) {
		List<Cookie> cookieList = chromeSession.getCommand().getPage().getCookies();
		
		StringBuilder sendCookie = new StringBuilder();
	    for (Cookie cookie : cookieList) {
			sendCookie.append(cookie.getName() + "=" + cookie.getValue() + ";");
		}
	    
	    // System.out.println("........cookie:" + sendCookie);
        
	    // https://jobads.zhaopin.com/Position/PositionAdd
	    String addUrl = "https://jobads.zhaopin.com/Position/PositionAdd";
	    RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();        
        headers.add("Accept", "application/xml, text/xml, */*");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Cookie", sendCookie.toString());
        
        
               
        HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> r = restTemplate.exchange(addUrl, HttpMethod.GET, formEntity, String.class, "");
		
        
		String str = r.getBody();
		//System.out.println("xxxxxxxxxxxxxxxxx:body:" + str);
		
		List<Cookie> lastCookieList = chromeSession.getCommand().getPage().getCookies();
		StringBuilder lastCookie = new StringBuilder();
	    for (Cookie cookie : lastCookieList) {
	    	lastCookie.append(cookie.getName() + "=" + cookie.getValue() + ";");
		}
	    
	    
	    System.out.println("xxxxxxxxxxxxxxxxx:lastCookie:" + lastCookie);
	    
	    Map<String, Object> returnMap = ControllerReturn.ok();
	    returnMap.put("result", 1);
	    returnMap.put("cookie", lastCookie);
		
		return returnMap;
	}
	
	
	
	private void click(double x, double y) {
		Input input = chromeSession.getCommand().getInput();
		input.dispatchMouseEvent(MousePressed, x, y, null, null, Left, 1, null, null);
        input.dispatchMouseEvent(MouseReleased, x, y, null, null, Left, 1, null, null);
	}
	
	
	public static boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {}
        }
        return true;
    }
	
	
	
}

