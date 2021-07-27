package com.jhs.exam.exam2.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.jhs.exam.exam2.app.App;

@WebListener
public class InitListener implements ServletContextListener {
	// 웹이 시작할때 한번 실행
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	App.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
