<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="메인" />
<%@ include file="../part/head.jspf"%>

<section class="section section-home-main">
  <div class="container mx-auto card-wrap px-4">
    <div class="card bordered shadow-lg">
      <div class="card-title">
        <a>
          <i class="fas fa-home-left"></i>
        </a>
        <span>홈</span>
      </div>

      <div class="px-4 py-4">
        <div class="flex items-center w-full px-4 py-10 bg-cover card bg-base-200"
          style="background-image: url(&quot;https://picsum.photos/id/180/1000/300&quot;);">
          <div class="card glass lg:card-side text-neutral-content">
            <figure class="p-6">
              <img src="https://picsum.photos/id/160/300/200" class="rounded-lg shadow-lg">
            </figure>
            <div class="max-w-md card-body">
              <h2 class="card-title_1">Cho</h2>
              <p>이 웹 사이트는 JSP로 간단한 로직을 이용하여 만든 웹 사이트로 개인 공부를 위해 만들어진 사이트 입니다.</p>
              <div class="card-actions">
                <button class="btn glass rounded-full">Get Started</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

<%@ include file="../part/foot.jspf"%>