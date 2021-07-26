package com.jhs.exam.exam2.http.controller;

import java.util.List;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Article;
import com.jhs.exam.exam2.dto.Board;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.ArticleService;
import com.jhs.exam.exam2.service.BoardService;
import com.jhs.exam.exam2.util.Ut;

public class UsrArticleController extends Controller {
	// articleService와 boardService를 사용하기 위해 Container에 생성된 해당 객체 불러오기
	private ArticleService articleService = Container.articleService;
	private BoardService boardService = Container.boardService;

	@Override
	public void performAction(Rq rq) {
		// ActionMethodName이 아래 case와 일치하면 해당 함수로 이동
		switch (rq.getActionMethodName()) {
		case "list":
			actionShowList(rq);
			break;
		case "detail":
			actionShowDetail(rq);
			break;
		case "write":
			actionShowWrite(rq);
			break;
		case "doWrite":
			actionDoWrite(rq);
			break;
		case "modify":
			actionShowModify(rq);
			break;
		case "doModify":
			actionDoModify(rq);
			break;
		case "doDelete":
			actionDoDelete(rq);
			break;
		// 일치하지 않을시 오류메세지 출력후 break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	// jsp페이지에서 삭제 버튼 동작시 해당 삭제 함수로 이동
	private void actionDoDelete(Rq rq) {
		// 해당 게시물을 확인하는 id를 넘겨 받아 변수에 저장
		int id = rq.getIntParam("id", 0);
		// 해당 페이지에서 이동할 uri를 받아 저장 없을시 ../article/list 저장
		String redirectUri = rq.getParam("redirectUri", "../article/list");

		// 비정상적으로 접근하여 게시물id가 없을시 리턴
		if (id == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}

		// 로그인된 멤버와 해당 게시물의 id를 게시물을 작성자만 삭제,수정 할수 있도록 판단하는 함수
		Article article = articleService.getForPrintArticleById(rq.getLoginedMember(), id);
		
		// 해당 게시물이 존재하지 않으면 오류메세지 출력후 리턴
		if ( article == null ) {
			rq.historyBack(Ut.f("%d번 게시물이 존재하지 않습니다.", id));
			return;
		}
		
		// 접속한 member와 게시물 이용하여 삭제여부 판단하는 함수
		ResultData actorCanDelete = articleService.actorCanDelete(rq.getLoginedMember(), article);
		
		// actorCanDelete가 F-로 시작시 오류 메세지 출력후 리턴
		if(actorCanDelete.isFail()) {
			rq.historyBack(actorCanDelete.getMsg());
			return;
		}
		
		// 해당 id게시물 삭제 하는 함수
		articleService.delete(id);

		// 완료 메세지 출력후 저장된 페이지로 이동
		rq.replace(Ut.f("%d번 게시물을 삭제하였습니다.", id), redirectUri);
	}

	// 페이지에서 해당 게시물 상세보기 할시 해당 함수 동작
	private void actionShowDetail(Rq rq) {
		// 해당 게시물 번호 받아 저장
		int id = rq.getIntParam("id", 0);

		// 비정상적으로 게시물 번호 없이 접근시 리턴
		if (id == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}

		// 접속한 member와 게시물 번호를 이용 하여 수정,삭제 여부 게시물이 존재하는지 여부 확인하는 함수
		Article article = articleService.getForPrintArticleById(rq.getLoginedMember(), id);
		
		// 해당 번호의 게시물이 없을시 오류 메세지 출력후 리턴
		if ( article == null ) {
			rq.historyBack(Ut.f("%d번 게시물이 존재하지 않습니다.", id));
			return;
		}

		// 이동할 페이지에 article변수 사용 할 수 있도록 담는다
		rq.setAttr("article", article);
		// 해당 페이지로 이동하는 함수
		rq.jsp("usr/article/detail");
	}

	// 페이지에서 게시물 리스트 보여줄때 동작하는 함수
	private void actionShowList(Rq rq) {
		// 게시판 번호를 지정하면 받아 저장 없을시 0을 저장
		int boardId = rq.getIntParam("boardId", 0);
		
		// 검색타입을 해당 변수에 저장 없을시 기본값 title,body 저장
		String searchKeywordTypeCode = rq.getParam("searchKeywordTypeCode", "title,body");
		// 해당 검색값 없을시 ""저장
		String searchKeyword = rq.getParam("searchKeyword", "");
		// 보여줄 게시물 갯수
		int itemsCountInAPage = 5;
		// 페이징 번호 없을시 첫번째 페이지 1 저장
		int page = rq.getIntParam("page", 1);
		
		// 검색타입, 검색값, 선택한 게시판 번호를 이용하여 해당되는 게시물수를 리턴하는 함수 후에 저장
		int totalItemsCount = articleService.getArticlesCount(searchKeywordTypeCode, searchKeyword, boardId);
		// 접속한 member와 해당 변수를 넣어 해당되는 게시물리스트를 리턴하는 함수 후에 저장
		List<Article> articles = articleService.getForPrintArticles(rq.getLoginedMember(), itemsCountInAPage, page, searchKeywordTypeCode, searchKeyword, boardId);
		
		// 총 게시물수에서 보여줄 itemsCountInAPage 나눈뒤 저장(나눌때 소숫점까지 나오게 double사용후 올림후 int로 변환)
		int totalPage = (int)Math.ceil((double)totalItemsCount / itemsCountInAPage); //  마지막 페이징수
		
		// boardId를 이용하여 해당 boardId에 맞는 board 불러와 저장
		Board board = boardService.getBoardById(boardId);
		
		// 필요한 변수들을 jsp에서 사용하도록 지정
		rq.setAttr("board", board);
		rq.setAttr("searchKeywordTypeCode", searchKeywordTypeCode);
		rq.setAttr("boardId", boardId);
		rq.setAttr("page", page);
		rq.setAttr("totalPage", totalPage);
		rq.setAttr("totalItemsCount", totalItemsCount);
		rq.setAttr("articles", articles);
		// 해당 페이지로 이동
		rq.jsp("usr/article/list");
	}

	// 게시물작성 jsp에서 연결(작성된 내용을 DB에 저장하는 함수)
	private void actionDoWrite(Rq rq) {
		// 게시물작성 jsp에서 넘어온 값을 변수에 저장
		int boardId = rq.getIntParam("boardId", 0);
		int memberId = rq.getLoginedMemberId();
		String title = rq.getParam("title", "");
		String body = rq.getParam("body", "");
		// 게시물작성 jsp에서 이동할 페이지 받아 저장 없을시 ../article/list 저장
		String redirectUri = rq.getParam("redirectUri", "../article/list");

		// 비정상적으로 접근하여 필요 변수가 존재 하지 않을시 리턴
		if (boardId == 0) {
			rq.historyBack("boadrId을 입력 해주세요.");
			return;
		}
		
		if (title.length() == 0) {
			rq.historyBack("title을 입력해주세요.");
			return;
		}

		if (body.length() == 0) {
			rq.historyBack("body를 입력해주세요.");
			return;
		}

		// 해당 변수를 이용하여 게시물 작성하는 함수 
		ResultData writeRd = articleService.write(boardId, memberId, title, body);
		// writeRd에 저장된 id값을 찾아 해당 변수에 저장
		int id = (int) writeRd.getBody().get("id");

		// [NEW_ID]이 해당 게시물의 id로 변환된다
		redirectUri = redirectUri.replace("[NEW_ID]", id + "");

		// 완료 메세지 출력후 해당 페이지로 이동
		rq.replace(writeRd.getMsg(), redirectUri);
	}

	// 해당 페이지로 이동하는 함수
	private void actionShowWrite(Rq rq) {
		rq.jsp("usr/article/write");
	}
	
	// 게시물 수정 페이지에서 연결(변경될 게시물 내용을 DB에 저장하는 함수)
	private void actionDoModify(Rq rq) {
		// 게시물 수정 페이지에서 필요한 값 변수에 담아 저장
		int id = rq.getIntParam("id", 0);
		String title = rq.getParam("title", "");
		String body = rq.getParam("body", "");
		// 게시물 수정 페이지에서 이동할 페이지값 저장 없을시 게시물상세페이지로 연결
		String redirectUri = rq.getParam("redirectUri", Ut.f("../article/detail?id=%d", id));
		
		// 비정상적으로 접근하여 필요 변수가 없을시 리턴
		if (id == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}

		if (title.length() == 0) {
			rq.historyBack("title을 입력해주세요.");
			return;
		}

		if (body.length() == 0) {
			rq.historyBack("body를 입력해주세요.");
			return;
		}
		
		// 접속한 member값과 게시물번호를 이용하여 article 구하고 저장하는 식
		Article article = articleService.getForPrintArticleById(rq.getLoginedMember(), id);
		
		// article이 존재하지 않을시 오류메세지 출력후 뒤로가기
		if(article == null) {
			rq.historyBack(Ut.f("%d번 게시물은 존재하지 않습니다.", id));
			return;
		}
		
		// 구해진 article과 접속한 member값을 이용하여 게시물 수정 가능여부 판단하는 함수(성공여부 리턴받아 저장)
		ResultData actorCanModify = articleService.actorCanModify(rq.getLoginedMember(), article);
		
		// actorCanModify값이 F-로 시작시 오류메세지 출력후 리턴
		if(actorCanModify.isFail()) {
			rq.historyBack(actorCanModify.getMsg());
			return;
		}

		// 수정 가능할시 게시물번호와 수정할 제목,내용을 이용하여 해당 게시물을 변경하는 함수(성공여부 리턴받아 저장)
		ResultData modifyRd = articleService.modify(id, title, body);

		// 성공메세지 출력후 해당 페이지로 이동하는 함수
		rq.replace(modifyRd.getMsg(), redirectUri);
	}

	// 수정할 게시물이 존재하는 확인하는 함수
	private void actionShowModify(Rq rq) {
		// 수정할 게시물을 id를 받아옴 없을시 0저장
		int id = rq.getIntParam("id", 0);

		// 해당 게시물 번호를 못받아올시 오류메세지 출력후 뒤로가기
		if (id == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}

		// 접속한 member값과 해당 게시물번호를 이용하여 article 구하여 리턴하는 함수(해당 값을 article변수에 저장)
		Article article = articleService.getForPrintArticleById(rq.getLoginedMember(), id);
		
		// 게시물이 없을시 오류메세지 출력후 뒤로가기
		if ( article == null ) {
			rq.historyBack(Ut.f("%d번 게시물이 존재하지 않습니다.", id));
			return;
		}
		
		// 접속한 member값과 구해진 게시물값으로 수정여부 판단하는 함수(성공여부 리턴받아 저장)
		ResultData actorCanModify = articleService.actorCanModify(rq.getLoginedMember(), article);
		
		// actorCanModify값이 F-로 시작시 오류메세지 출력후 뒤로가기
		if(actorCanModify.isFail()) {
			rq.historyBack(actorCanModify.getMsg());
			return;
		}

		// 해당 jsp에서 사용할 article변수 보내기
		rq.setAttr("article", article);
		// 해당 페이지 이동하는 함수
		rq.jsp("usr/article/modify");
	}
}
