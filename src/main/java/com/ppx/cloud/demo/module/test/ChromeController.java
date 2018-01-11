package com.ppx.cloud.demo.module.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;


@Controller	
public class ChromeController {
	
	@GetMapping
	public ModelAndView chrome(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		return mv;
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
	
	
	private static Session chromeSession = null;
	
	private static SessionFactory sessionFactory = null;
	
	public static int DEFAULT_PORT = 9222;
	
	@GetMapping @ResponseBody
	public Map<String, Object> zhaopin(HttpServletRequest request) {
		
		try {
			System.out.println("----------------------zhaopin begin-------------------");
			
			List<String> arguments = new ArrayList<String>();
			arguments.add("--headless");
			arguments.add("--window-size=1920,1080");
			arguments.add("--disable-gpu");
			
			//SessionFactory sessionFactory = null;
			//System.gc();
			//System.gc();
			//chromeSession = null;
			if (chromeSession == null) {
				
				boolean isPortExist = isHostConnectable("localhost", SessionFactory.DEFAULT_PORT);
				
				System.out.println("----------------------002-------------------new Launcher(DEFAULT_PORT)");
				Launcher launcher = new Launcher();
				sessionFactory = launcher.launch(arguments);
				
				
				int len = sessionFactory.list().size();
				System.out.println("xxxxxxxlen:" + len);
				
				
				if (!isPortExist) {
					System.out.println("---------------------------x001");
					chromeSession = sessionFactory.create();
				}
				else if (len >= 1) {
					System.out.println("---------------------------x002");
					chromeSession = sessionFactory.connect(sessionFactory.list().get(0).getId());
				}
				else {
					System.out.println("---------------------------x003");
					chromeSession = sessionFactory.create();
				}
			}
			else {
				if (!isHostConnectable("localhost", SessionFactory.DEFAULT_PORT)) {
					System.gc();
					System.out.println("----------22222222-------------------:");
					Launcher launcher = new Launcher();
					sessionFactory = launcher.launch(arguments);
					chromeSession = sessionFactory.create();
					System.gc();
				}
			}
			
			
//			System.out.println("...............size:" + sessionFactory.list().size());
//			System.out.println("...............port:" + sessionFactory.getPort());
//			
//			
//			if (sessionFactory.list().size() == 1) {
//				System.out.println("----------------------003-------------------sessionFactory.list().size() == 1");
//				String id = sessionFactory.list().get(0).getId();
//				chromeSession = sessionFactory.connect(id);
//			}
//			else {
//				System.out.println("----------------------004----------------------sessionFactory.create();");
//				chromeSession = sessionFactory.create();
//			}
			
		
			// https://www.baidu.com/ 
			// https://passport.zhaopin.com/org/login
			chromeSession.navigate("https://www.baidu.com/");
			chromeSession.waitDocumentReady();
			chromeSession.activate();
		
			
			
			System.out.println("----------------------005 end-------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ControllerReturn.ok();
			
	}
	
	
	
	
	
	
}

