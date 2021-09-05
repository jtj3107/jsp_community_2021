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

/* 토스트 UI 시작*/

function getUriParams(uri) {
  uri = uri.trim();
  uri = uri.replaceAll('&amp;', '&');
  if ( uri.indexOf('#') !== -1 ) {
    let pos = uri.indexOf('#');
    uri = uri.substr(0, pos);
  }

  let params = {};

  uri.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
  return params;
}

function codepenPlugin() {
  const toHTMLRenderers = {
    codepen(node) {
      const html = renderCodepen(node.literal);

      return [
        { type: 'openTag', tagName: 'div', outerNewLine: true },
        { type: 'html', content: html },
        { type: 'closeTag', tagName: 'div', outerNewLine: true }
      ];
    }
  }
  
  function renderCodepen(uri) {    
    let uriParams = getUriParams(uri);

    let height = 400;

    let preview = '';

    if ( uriParams.height ) {
      height = uriParams.height;
    }

    let width = '100%';

    if ( uriParams.width ) {
      width = uriParams.width;
    }

    if ( !isNaN(width) ) {
      width += 'px';
    }

    let iframeUri = uri;

    if ( iframeUri.indexOf('#') !== -1 ) {
      let pos = iframeUri.indexOf('#');
      iframeUri = iframeUri.substr(0, pos);
    }

    return '<iframe height="' + height + '" style="width: ' + width + ';" scrolling="no" title="" src="' + iframeUri + '" frameborder="no" allowtransparency="true" allowfullscreen="true"></iframe>';
  }

  return { toHTMLRenderers }
}
// 유튜브 플러그인 끝

// repl 플러그인 시작
function replPlugin() {
  const toHTMLRenderers = {
    repl(node) {
      const html = renderRepl(node.literal);

      return [
        { type: 'openTag', tagName: 'div', outerNewLine: true },
        { type: 'html', content: html },
        { type: 'closeTag', tagName: 'div', outerNewLine: true }
      ];
    }
  }
  
  function renderRepl(uri) {
    var uriParams = getUriParams(uri);

    var height = 400;

    if ( uriParams.height ) {
      height = uriParams.height;
    }

    return '<iframe frameborder="0" width="100%" height="' + height +'px" src="' + uri + '"></iframe>';
  }

  return { toHTMLRenderers }
}

function youtubePlugin() {
  const toHTMLRenderers = {
    youtube(node) {
      const html = renderYoutube(node.literal);

      return [
        { type: 'openTag', tagName: 'div', outerNewLine: true },
        { type: 'html', content: html },
        { type: 'closeTag', tagName: 'div', outerNewLine: true }
      ];
    }
  }

  function renderYoutube(uri) {
    uri = uri.replace('https://www.youtube.com/watch?v=', '');
    uri = uri.replace('http://www.youtube.com/watch?v=', '');
    uri = uri.replace('www.youtube.com/watch?v=', '');
    uri = uri.replace('youtube.com/watch?v=', '');
    uri = uri.replace('https://youtu.be/', '');
    uri = uri.replace('http://youtu.be/', '');
    uri = uri.replace('youtu.be/', '');

    let uriParams = getUriParams(uri);

    let width = '100%';
    let height = '100%';

    let maxWidth = 500;

    if ( !uriParams['max-width'] && uriParams['ratio'] == '9/16' ) {
      uriParams['max-width'] = 300;
    }

    if ( uriParams['max-width'] ) {
      maxWidth = uriParams['max-width'];
    }

    let ratio = '16/9';

    if ( uriParams['ratio'] ) {
      ratio = uriParams['ratio'];
    }

    let marginLeft = 'auto';

    if ( uriParams['margin-left'] ) {
      marginLeft = uriParams['margin-left'];
    }

    let marginRight = 'auto';

    if ( uriParams['margin-right'] ) {
      marginRight = uriParams['margin-right'];
    }

    let youtubeId = uri;

    if ( youtubeId.indexOf('?') !== -1 ) {
      let pos = uri.indexOf('?');
      youtubeId = youtubeId.substr(0, pos);
    }

    return '<div style="max-width:' + maxWidth + 'px; margin-left:' + marginLeft + '; margin-right:' + marginRight + ';" class="ratio-' + ratio + ' relative"><iframe class="absolute top-0 left-0 w-full" width="' + width + '" height="' + height + '" src="https://www.youtube.com/embed/' + youtubeId + '" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe></div>';
  }
  // 유튜브 플러그인 끝

  return { toHTMLRenderers }
}

// katex 플러그인
function katexPlugin() {
  const toHTMLRenderers = {
    katex(node) {
      let html = katex.renderToString(node.literal, {
        throwOnError: false
      });

      return [
        { type: 'openTag', tagName: 'div', outerNewLine: true },
        { type: 'html', content: html },
        { type: 'closeTag', tagName: 'div', outerNewLine: true }
      ];
    },
  }

  return { toHTMLRenderers }
}

const ToastEditor__chartOptions = {
  minWidth: 100,
  maxWidth: 600,
  minHeight: 100,
  maxHeight: 300
};

function ToastEditor__init() {
  $('.toast-ui-editor').each(function(index, node) {
    const $node = $(node);
    const $initialValueEl = $node.find(' > script');
    const initialValue = $initialValueEl.length == 0 ? '' : $initialValueEl.html().trim();

    const editor = new toastui.Editor({
      el: node,
      previewStyle: 'vertical',
      initialValue: initialValue,
      height:'600px',
      plugins: [
        [toastui.Editor.plugin.chart, ToastEditor__chartOptions],
        [toastui.Editor.plugin.codeSyntaxHighlight, {highlighter:Prism}],
        toastui.Editor.plugin.colorSyntax,
        toastui.Editor.plugin.tableMergedCell,
        toastui.Editor.plugin.uml,
        katexPlugin,
        youtubePlugin,
        codepenPlugin,
        replPlugin
      ],
      customHTMLSanitizer: html => {
        return DOMPurify.sanitize(html, { ADD_TAGS: ["iframe"], ADD_ATTR: ['width', 'height', 'allow', 'allowfullscreen', 'frameborder', 'scrolling', 'style', 'title', 'loading', 'allowtransparency'] }) || ''
      }
    });

    $node.data('data-toast-editor', editor);
  });
}

function ToastEditorView__init() {
  $('.toast-ui-viewer').each(function(index, node) {
    const $node = $(node);
    const $initialValueEl = $node.find(' > script');
    const initialValue = $initialValueEl.length == 0 ? '' : $initialValueEl.html().trim();
    $node.empty();

    let viewer = new toastui.Editor.factory({
      el: node,
      initialValue: initialValue,
      viewer:true,
      plugins: [
        [toastui.Editor.plugin.codeSyntaxHighlight, {highlighter:Prism}],
        toastui.Editor.plugin.colorSyntax,
        toastui.Editor.plugin.tableMergedCell,
        toastui.Editor.plugin.uml,
        katexPlugin,
        youtubePlugin,
        codepenPlugin,
        replPlugin
      ],
      customHTMLSanitizer: html => {
        return DOMPurify.sanitize(html, { ADD_TAGS: ["iframe"], ADD_ATTR: ['width', 'height', 'allow', 'allowfullscreen', 'frameborder', 'scrolling', 'style', 'title', 'loading', 'allowtransparency'] }) || ''
      }
    });

    $node.data('data-toast-editor', viewer);
  });
}

$(function() {
  ToastEditor__init();
  ToastEditorView__init();
});

/* 토스트 UI 끝*/

MobileTopBar__init();
MobileSideBar__init();
TopBar__init();
SearchBar__init();