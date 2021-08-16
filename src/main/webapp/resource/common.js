console.log("common.js 로딩");

/* 모바일 탑바 시작 */
function MobileTopBar__init() {
  $('.mobile-top-bar .btn-show-mobile-side-bar').click(function() {
    MobileSideBar__show();
  });
  
  $('.mobile-top-bar .btn-show-search-bar').click(function() {
    SearchBar__show();
  });
}
/* 모바일 탑바 끝 */

/* 모바일 사이드바 시작 */
function MobileSideBar__init() {
  $('.mobile-side-bar, .mobile-side-bar .btn-close-mobile-side-bar').click(function() {
    MobileSideBar__hide();
  })
  
  $('.mobile-side-bar__content').click(function(e) {
		e.stopPropagation();
	});
  
  $('.mobile-side-bar .btn-show-search-bar').click(function() {
    SearchBar__show();
  });
}

function MobileSideBar__show() {
  $('.mobile-side-bar').addClass('active');
  $('html').addClass('mobile-side-bar-actived');
}

function MobileSideBar__hide() {
  $('.mobile-side-bar').removeClass('active');
  $('html').removeClass('mobile-side-bar-actived');
}
/* 모바일 사이드바 끝 */

/* 탑바 시작 */
function TopBar__init() {  
  $('.top-bar .btn-show-search-bar').click(function() {
    SearchBar__show();
  });
}
/* 탑바 끝 */

/* 검색바 시작 */
function SearchBar__init() {
  $('.search-bar .btn-hide-search-bar').click(function() {
    SearchBar__hide();
  });
}

function SearchBar__show() {
  $('html').addClass('search-bar-actived');
  $('.search-bar').addClass('active');
  
  setTimeout(function() {
	$('.search-bar form input[name="searchKeyword"]').focus();
	}, 100);
}

function SearchBar__hide() {
  $('html').removeClass('search-bar-actived');
  $('.search-bar').removeClass('active');
  $('.search-bar form input[name="searchKeyword"]').val('');
}
/* 검색바 끝 */

/* 이메일 인증 발송 시작 */
function emailSend() {
	let clientEmail = document.getElementById('emailText').value;
	let emailYN = isEmail(clientEmail);
	
	console.log('입력 이메일' + clientEmail);
	
	if(emailYN == true) { 	
		var data = "clientEmail=" + clientEmail;
		
		$.ajax({
			type: "POST",
			url: "../home/doSendMail",
			data : data,
			dataType : "html",
			
			success : function(html){
				alert('이메일로 인증번호를 발송했습니다.');
			},error : function(xhr){
				alert('오류입니다. 잠시 후 다시 시도해주세요.');
			}
		});
	} else {
		alert('이메일 형식에 맞게 입력해주세요 xxx@xxx.com');
	}

}

function isEmail(asValue) {
	var r = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    return r.test(asValue);
}

/* 이메일 인증 발송 끝 */

/* 이메일 인증 시작 */
function emailCertification() {
	let certificationNumber = document.getElementById('certificationNumber').value;
	
	var data = "certificationNumber=" + certificationNumber;
		$.ajax({
			type: "POST",
			url: "../home/doEmailCertification",
			data : data,
			dataType : "html",
			
			success : function(html){
				alert('인증완료');
				document.getElementById('certificationYN').value = "true";
			},error : function(xhr){
				alert('오류입니다. 잠시 후 다시 시도해주세요.');
			}
		});
}

/* 이메일 인증 끝 */

MobileTopBar__init();
MobileSideBar__init();
TopBar__init();
SearchBar__init();