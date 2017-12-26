package com.ppx.cloud.demo.module.test;

import static io.webfolder.cdp.type.constant.MouseButtonType.Left;
import static io.webfolder.cdp.type.constant.MouseEventType.MousePressed;
import static io.webfolder.cdp.type.constant.MouseEventType.MouseReleased;
import static java.lang.String.format;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.command.DOM;
import io.webfolder.cdp.command.Input;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.type.constant.ImageFormat;
import io.webfolder.cdp.type.dom.BoxModel;
import io.webfolder.cdp.type.page.Viewport;


@Controller	
public class ChromeBackupController {
	
	private static Session staticSession = null;
	
	@GetMapping @ResponseBody
	public Map<String, Object> test(HttpServletRequest request) {
		Launcher launcher = new Launcher();
		try {
			List<String> arguments = new ArrayList<String>();
			//arguments.add("--headless");
			//arguments.add("--disable-gpu");
		
			SessionFactory factory = launcher.launch(arguments);
			
			// 只打开一个窗口
			if (staticSession == null || !staticSession.isConnected()) {
				staticSession = factory.create();
			}
			
			staticSession.navigate("https://passport.zhaopin.com/org/login");
			staticSession.waitDocumentReady();
			staticSession.activate();
					
			staticSession.evaluate("$('#CheckCodeCapt').click();");
			staticSession.wait(1000);
	        
		    // byte[] data = staticSession.captureScreenshot();
			DOM dom = staticSession.getCommand().getDOM();
			Integer nodeId = staticSession.getNodeId("#captcha");
			
			BoxModel boxModel = dom.getBoxModel(nodeId, null, null);
			System.out.println("xxxxxxxxx...........w:" + boxModel.getWidth());
			System.out.println("xxxxxxxxx...........w:" + boxModel.getHeight());			
			System.out.println("xxxxxxxxx...........w:" + boxModel.getContent());
		    // 
		    Viewport clip = new Viewport();
		    clip.setScale(1d);
		    clip.setX(boxModel.getContent().get(0));
		    clip.setY(boxModel.getContent().get(5) - 50d);
		    clip.setWidth(boxModel.getWidth().doubleValue());
		    clip.setHeight(boxModel.getHeight().doubleValue() - 50d);
		    
		    byte[] data = staticSession.getCommand().getPage().captureScreenshot(ImageFormat.Png, 100, clip, true);
		    
		    
		    try {
		    	FileOutputStream out = new FileOutputStream(new File("E:/U/png/clip01.png")); 
		    	out.write(data);
		    	out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		    request.getSession().setAttribute("chromeSession", staticSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ControllerReturn.ok();
		    
	}
	
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> init(HttpServletRequest request) {
		System.out.println("xxxxxxxxxxxxxxx----------init");
	
		
		Launcher launcher = new Launcher();
		try {
			List<String> arguments = new ArrayList<String>();
			//arguments.add("--headless");
			
			SessionFactory factory = launcher.launch(arguments);
			staticSession = factory.create();
			staticSession.navigate("https://passport.zhaopin.com/org/login");
			staticSession.waitDocumentReady();
			staticSession.activate();
						
		    
			staticSession.wait(200);
		    Input input = staticSession.getCommand().getInput();
		    
	        input.dispatchMouseEvent(MousePressed, 666d, 334d, null, null, Left, 1, null, null);
	        input.dispatchMouseEvent(MouseReleased, 666d, 334d, null, null, Left, 1, null, null);
	        
	        
	        // 输入用户名和密码
	        staticSession.evaluate("$('#LoginName').val('honghai020');");
	        staticSession.evaluate("$('#Password').val('Test13800');");
	        
	        staticSession.wait(1500);
	        
	        
	        
		    byte[] data = staticSession.captureScreenshot();
		    
		    try {
		    	FileOutputStream out = new FileOutputStream(new File("E:/U/png/1.png")); 
		    	out.write(data);
		    	out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		    request.getSession().setAttribute("chromeSession", staticSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ControllerReturn.ok();
		    
	}
	
	// click736,377;816,379;760,376;813,478
	@GetMapping @ResponseBody
	public Map<String, Object> click(HttpServletRequest request, String points) {
		String[] point = points.split(";");
		Input input = staticSession.getCommand().getInput();
		double x = 190;
        input.dispatchMouseEvent(MousePressed, Double.parseDouble(point[0].split(",")[0]) - x, Double.parseDouble(point[0].split(",")[1]), null, null, Left, 1, null, null);
        input.dispatchMouseEvent(MouseReleased, Double.parseDouble(point[0].split(",")[0]) - x, Double.parseDouble(point[0].split(",")[1]), null, null, Left, 1, null, null);
        staticSession.wait(100);
        input.dispatchMouseEvent(MousePressed, Double.parseDouble(point[1].split(",")[0]) - x, Double.parseDouble(point[1].split(",")[1]), null, null, Left, 1, null, null);
        input.dispatchMouseEvent(MouseReleased, Double.parseDouble(point[1].split(",")[0]) - x, Double.parseDouble(point[1].split(",")[1]), null, null, Left, 1, null, null);
        staticSession.wait(100);
        input.dispatchMouseEvent(MousePressed, Double.parseDouble(point[2].split(",")[0]) - x, Double.parseDouble(point[2].split(",")[1]), null, null, Left, 1, null, null);
        input.dispatchMouseEvent(MouseReleased, Double.parseDouble(point[2].split(",")[0]) - x, Double.parseDouble(point[2].split(",")[1]), null, null, Left, 1, null, null);
        staticSession.wait(100);
        
        // 1034,569
        input.dispatchMouseEvent(MousePressed, 1036d - x, 568d, null, null, Left, 1, null, null);
        input.dispatchMouseEvent(MouseReleased, 1036d - x, 568d, null, null, Left, 1, null, null);
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
        
		
		return ControllerReturn.ok();
	}
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> login(HttpServletRequest request) {
        // 登录894,446
		double x = 190;
		Input input = staticSession.getCommand().getInput();
        //input.dispatchMouseEvent(MousePressed, 894d - x, 684d, null, null, Left, 1, null, null);
        //input.dispatchMouseEvent(MouseReleased, 894d - x, 684d, null, null, Left, 1, null, null);
        
        staticSession.evaluate("$('#loginbutton').submit();");        
        staticSession.wait(3000);
        
        staticSession.getCommand().getPage().getCookies();
   
		
        byte[] data = staticSession.captureScreenshot();
	    
	    try {
	    	FileOutputStream out = new FileOutputStream(new File("E:/U/png/3.png")); 
	    	out.write(data);
	    	out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		
		return ControllerReturn.ok();
	}
	
	
	
	
//	@GetMapping @ResponseBody
//	public Map<String, Object> test() {
//		System.out.println("xxxxxxxxxxxxxxx----------test");
//		
//		
//	
//		Launcher launcher = new Launcher();
//		try (SessionFactory factory = launcher.launch();
//		                    Session session = factory.create()) {
//		    session.navigate("https://passport.zhaopin.com/org/login");
//		    session.waitDocumentReady();
//		 
//		    
//		    //String content = (String) session.getProperty("//body", "outerText");
//		    //System.out.println(session.getContent());
//		    
//		    // activate the tab/session before capturing the screenshot
//		    session.activate();
//		    session.wait(1000);
//		    
//		    //session.click("#CheckCodeCapt");
//		    
//		    Input input = session.getCommand().getInput();
//	        input.dispatchMouseEvent(MousePressed, 666d, 334d, null, null, Left, 1, null, null);
//	        input.dispatchMouseEvent(MouseReleased, 666d, 334d, null, null, Left, 1, null, null);
//	        
//	        session.wait(1200);
//	        
//	        input.dispatchMouseEvent(MousePressed, 646d, 488d, null, null, Left, 1, null, null);
//	        input.dispatchMouseEvent(MouseReleased, 646d, 488d, null, null, Left, 1, null, null);
//	        
//		    
//		    //session.evaluate("$('#CheckCodeCapt').click()");
//		    
//		    
//		    session.wait(100);
//		    
//		    
//		    byte[] data = session.captureScreenshot();
//		    
//		    
//		    try {
//		    	FileOutputStream out = new FileOutputStream(new File("E:/U/png/1.png")); 
//		    	out.write(data);
//		    	out.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		    
//		    //System.out.println("xxxxxxxout," + session.getContent());
//		    
//		    
//		}
//		
//		return ControllerReturn.ok();
//	}
	
	
	
}

