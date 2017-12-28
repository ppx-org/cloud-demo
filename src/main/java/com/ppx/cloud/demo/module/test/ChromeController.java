package com.ppx.cloud.demo.module.test;

import static io.webfolder.cdp.type.constant.MouseButtonType.Left;
import static io.webfolder.cdp.type.constant.MouseEventType.MousePressed;
import static io.webfolder.cdp.type.constant.MouseEventType.MouseReleased;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.command.DOM;
import io.webfolder.cdp.command.Input;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.type.dom.BoxModel;
import io.webfolder.cdp.type.network.Cookie;
import io.webfolder.cdp.type.page.Viewport;


@Controller	
public class ChromeController {
	
	private static Session staticSession = null;
	
	
	
	@GetMapping
	public ModelAndView chrome(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		return mv;
	}
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> test(HttpServletRequest request) {
		Launcher launcher = new Launcher();
		try {
			List<String> arguments = new ArrayList<String>();
			arguments.add("--headless");
			//arguments.add("--disable-gpu");
		
			SessionFactory factory = launcher.launch(arguments);
			//launcher.getProcessManager().kill();
			
			// 只打开一个窗口
			if (staticSession == null || !staticSession.isConnected()) {
				staticSession = factory.create();
			}
			
			
			staticSession.navigate("https://passport.zhaopin.com/org/login");
			staticSession.waitDocumentReady();
			staticSession.activate();
			
			staticSession.wait(200);
			//click(666, 334);
			staticSession.evaluate("$('#CheckCodeCapt').click();");
			staticSession.wait(1200);
	        
//		    // byte[] data = staticSession.captureScreenshot();
			
			
			DOM dom = staticSession.getCommand().getDOM();
			Integer nodeId = staticSession.getNodeId("#captcha");
			BoxModel boxModel = dom.getBoxModel(nodeId, null, null);
			System.out.println("xxxxxxxxx...........w:" + boxModel.getWidth());
			System.out.println("xxxxxxxxx...........w:" + boxModel.getHeight());			
			System.out.println("xxxxxxxxx...........w:" + boxModel.getContent());
//		    // 
		    Viewport clip = new Viewport();
		    clip.setScale(0d);
		    clip.setX(66d);
		    clip.setY(439d);
		    clip.setWidth(290d);
		    clip.setHeight(500d);
		    
		    
		    
		    byte[] data = staticSession.captureScreenshot();
		    //byte[] data = staticSession.getCommand().getPage().captureScreenshot(ImageFormat.Png, 100, clip, true);
		    //byte[] data = tmpPage.captureScreenshot(ImageFormat.Png, 100, clip, true);
		    
		    try {
		    	//FileOutputStream out = new FileOutputStream(new File("E:/U/png/clip01.png")); 
		    	FileOutputStream out = new FileOutputStream(new File("E:/Git/ppx-org/cloud-demo/target/classes/static/test/clip01.png"));
		    	
		    	out.write(data);
		    	out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		    //request.getSession().setAttribute("chromeSession", staticSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ControllerReturn.ok();
	}
	
	private void click(double x, double y) {
		Input input = staticSession.getCommand().getInput();
		input.dispatchMouseEvent(MousePressed, x, y, null, null, Left, 1, null, null);
        input.dispatchMouseEvent(MouseReleased, x, y, null, null, Left, 1, null, null);
	}
	
	@GetMapping @ResponseBody
	public Map<String, Object> click(HttpServletRequest request, String points) {
		// 601.0, 448.0,
		// double offsetX = 477 + 15; // 有头
		double offsetX = 477 + 15 + 43; // 无头
		double offsetY = 337 + 25 - 2;
		
		
		String[] point = points.split(";");
		double x1 = Double.parseDouble(point[0].split(",")[0]) + offsetX;
		double y1 = Double.parseDouble(point[0].split(",")[1]) + offsetY;
		double x2 = Double.parseDouble(point[1].split(",")[0]) + offsetX;
		double y2 = Double.parseDouble(point[1].split(",")[1]) + offsetY;
		double x3 = Double.parseDouble(point[2].split(",")[0]) + offsetX;
		double y3 = Double.parseDouble(point[2].split(",")[1]) + offsetY;
		
		
		click(x1, y1);
		staticSession.wait(100);
		click(x2, y2);
		staticSession.wait(100);
		click(x3, y3);
		staticSession.wait(100);
		
		// 输入用户名和密码
		staticSession.evaluate("$('#LoginName').val('honghai020');");
		staticSession.evaluate("$('#Password').val('Test13800');");
		staticSession.evaluate("$('#captcha-submitCode').click();");
        staticSession.wait(1000);
        
        // 登录
        staticSession.evaluate("$('#loginbutton').click();");  
        staticSession.wait(3000);
        
        
        
		
        byte[] data = staticSession.captureScreenshot();
	    
	    try {
	    	FileOutputStream out = new FileOutputStream(new File("E:/U/png/2.png")); 
	    	out.write(data);
	    	out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	    List<Cookie> cookieList = staticSession.getCommand().getPage().getCookies();
	    for (Cookie cookie : cookieList) {
			System.out.println("........cookie:" + cookie.getName() + ":" + cookie.getValue());
		}
		
		return ControllerReturn.ok();
	}
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> cookie(HttpServletRequest request) {
		List<Cookie> cookieList = staticSession.getCommand().getPage().getCookies();
		
		StringBuilder sendCookie = new StringBuilder();
	    for (Cookie cookie : cookieList) {
			System.out.println("........cookie:" + cookie.getName() + ":" + cookie.getValue());
			sendCookie.append(cookie.getName() + "=" + cookie.getValue() + ";");
		}
        
	    //https://jobads.zhaopin.com/Position/PositionAdd
	    String addUrl = "https://jobads.zhaopin.com/Position/PositionAdd";
	    RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();        
        headers.add("Accept", "application/xml, text/xml, */*");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Origin", "https://ehire.51job.com");
        headers.add("Referer", "https://ehire.51job.com/");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Cookie", sendCookie.toString());
        
        
               
        HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> r = restTemplate.exchange(addUrl, HttpMethod.GET, formEntity, String.class, "");
		
        
		String str = r.getBody();
		System.out.println("xxxxxxxxxxxxxxxxx:body:" + str);
	    
		
		return ControllerReturn.ok();
	}
	
	
	
	
	
}

