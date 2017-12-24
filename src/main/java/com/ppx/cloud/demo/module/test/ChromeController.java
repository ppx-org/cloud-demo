package com.ppx.cloud.demo.module.test;

import static io.webfolder.cdp.type.constant.MouseButtonType.Left;
import static io.webfolder.cdp.type.constant.MouseEventType.MousePressed;
import static io.webfolder.cdp.type.constant.MouseEventType.MouseReleased;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.controller.ControllerReturn;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.command.Input;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;


@Controller	
public class ChromeController {
	
	
	@GetMapping @ResponseBody
	public Map<String, Object> test() {
		System.out.println("xxxxxxxxxxxxxxx----------begin");
		
		Launcher launcher = new Launcher();
		try (SessionFactory factory = launcher.launch();
		                    Session session = factory.create()) {
		    session.navigate("https://passport.zhaopin.com/org/login");
		    session.waitDocumentReady();
		 
		    
		    //String content = (String) session.getProperty("//body", "outerText");
		    //System.out.println(session.getContent());
		    
		    // activate the tab/session before capturing the screenshot
		    session.activate();
		    session.wait(1000);
		    
		    //session.click("#CheckCodeCapt");
		    
		    Input input = session.getCommand().getInput();
	        input.dispatchMouseEvent(MousePressed, 666d, 334d, null, null, Left, 1, null, null);
	        input.dispatchMouseEvent(MouseReleased, 666d, 334d, null, null, Left, 1, null, null);
	        
	        session.wait(1200);
	        
	        input.dispatchMouseEvent(MousePressed, 646d, 488d, null, null, Left, 1, null, null);
	        input.dispatchMouseEvent(MouseReleased, 646d, 488d, null, null, Left, 1, null, null);
	        
		    
		    //session.evaluate("$('#CheckCodeCapt').click()");
		    
		    
		    session.wait(100);
		    
		    
		    byte[] data = session.captureScreenshot();
		    
		    
		    try {
		    	FileOutputStream out = new FileOutputStream(new File("E:/U/png/1.png")); 
		    	out.write(data);
		    	out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		    //System.out.println("xxxxxxxout:" + session.getContent());
		    
		    
		}
		
		return ControllerReturn.ok();
	}
	
	
	
}

