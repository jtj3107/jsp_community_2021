package com.jhs.exam.exam2.service;

import java.util.List;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.MemberRepository;
import com.jhs.exam.exam2.util.Ut;

public class MemberService {
	private MemberRepository memberRepository = Container.memberRepository;
	// private EmailService emailService = Container.emailService;

	public ResultData login(String loginId, String loginPw) {
		// 로그인아이디로 member가 존재하는지 확인하는 함수
		Member member = memberRepository.getMemberByLoginId(loginId);

		// 해당 member가 존재 하지 않을시 F-1저장후 리턴
		if (member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원의 로그인아이디 입니다.");
		}

		// 해당 member는 존재 하나 member의 비밀번호와 입력한 비밀번호가 틀릴시 F-2저장후 리턴
		if (member.getLoginPw().equals(loginPw) == false) {
			return ResultData.from("F-2", "비밀번호가 일치하지 않습니다.");
		}

		// 모두 이상 없을시 S-1, member값 저장후 리턴
		return ResultData.from("S-1", "환영합니다.", "member", member);
	}

	public ResultData join(String loginId, String loginPw, String name, String nickname, String email,
			String cellphoneNo) {
		// members를 구하는 함수
		List<Member> members = getForPrintMembers();
		
		// for문을 통해 members에 있는 로그인아이디와 입력받은 로그인아이디를 비교하여
		for(Member member : members) {
			if(member.getLoginId().equals(loginId)) {
				// 로그인아이디가 이미 존재하면 F-1 리턴
				return ResultData.from("F-1", Ut.f("이미 사용중인 아이디 입니다."));
			} else if (member.getName().equals(name) && member.getEmail().equals(email)) {
				// 각각 멤버의 이름과 이메일로 가입된 회원이 있을시 F-2 리턴
				return ResultData.from("F-2", Ut.f("이미 가입된 회원입니다."));
			}
		}
		
		// 위 if 해당되지 않으면 회원가입 함수 실행
		memberRepository.join(loginId, loginPw, name, nickname, email, cellphoneNo);

		// S-1, 완료 메세지 저장후 리턴
		return ResultData.from("S-1", Ut.f("회원가입이 완료되었습니다."));
	}

	public List<Member> getForPrintMembers() {
		// members를 구하는 함수
		return memberRepository.getForPrintMembers();
	}
	
	// 해당 멤버를 찾아 메세지와 함께 리턴하는 함수
	public ResultData getMemberByNameAndEmail(String name, String email) {
		// 컨트롤러에서 받은 name과 email로 해당 member 구하는 함수
		Member member = memberRepository.getMemberByNameAndEmail(name, email);
			
		// 해당 member가 존재하지 않으면 F-1저장후 리턴
		if(member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원입니다.");
		}
		
		// 멤버의 로그인 아이디
		String loginId = member.getLoginId();
		
		// S-1과 해당 member의 로그인아이디를 출력하는 메세지와 로그인 아이디 저장후 리턴
		return ResultData.from("S-1", Ut.f("해당 회원의 아이디는 [" + loginId + "] 입니다"), "loginId", loginId);
	}

	public ResultData getMemberByLoginIdAndEmail(String loginId, String email) {
		// 컨트롤러에서 받아온 로그인아이디, 이메일로 해당 멤버 구한뒤 저장
		Member member = memberRepository.getMemberByLoginIdAndEmail(loginId, email);
		
		// 존재하지 않을시 F-1저장후 리턴
		if(member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원입니다.");
		}
		
		// 해당 멤버의 이메일과 작성한 이메일이 다를시 F-2저장후 리턴
		if(member.getEmail().equals(email) == false) {
			return ResultData.from("F-2", "가입하신 이메일이 아닙니다.");
		}
		
		// 해당 멤버가 구해졌을시 S-1 저장후 메세지, member값 저장후 리턴
		return ResultData.from("S-1", Ut.f("확인 되었습니다."), "member", member);
	}

	public ResultData sendTempLoginPwToEmail(Member actor) {
		// 메일 제목과 내용 만들기
		String siteName = App.getSiteName(); // 사이트 이름 리턴하는 함수
		//String siteLoginUrl = App.getLoginUri();
		String title = "[" + siteName + "] 임시 패스워드 발송"; // 이메일 제목
		String tempPassword = Ut.getTempPassword(6); // 임시 비밀번호 저장
		String body = "<h1>임시 패스워드 : " + tempPassword + "<h1>"; // 내용
		// 내용 + 해당 사이트 로그인페이지로 이동하는 a태크 생성
		body += "<a href=\"" + "http://localhost:8084/jsp_community_2021/usr/member/login" + "\" target=\"_blank\">로그인 하러가기</a>";
	
		// 메일 발송(보내는 매개, 매개 비밀번호, 보내는사람, 사이트이름, 해당 멤버의 이메일, 제목, 내용)
		int sendRs = Ut.sendMail("jtj3926@gmail.com", "khnvjxnxfqxotpnv", "no-reply@lemon-cm.com", "레몬 커뮤니티 알림", actor.getEmail(), title, body);
		
		// sendRs이 1이면 발송 성공 1이 아니면 발송실패
		if( sendRs != 1) {
			return ResultData.from("F-1", "메일 발송에 실패하였습니다.");
		}
		
		// 해당 멤버와 임시 비밀번호를 이용하여 해당 멤버의 비밀번호를 변경하는 함수
		setTempPassword(actor, tempPassword);
		
		// 메일 발송이 완료되면 S-1, 해당 이메일로 발송했다는 메세지 저장후 리턴
		return ResultData.from("S-1", Ut.f("해당 회원의 새로운 비밀번호를" + actor.getEmail() + "로 발송하였습니다."));
	}

	private void setTempPassword(Member actor, String tempPassword) {
		// DB에 접근하여 해당 멤버 비밀번호 변경하는 함수
		memberRepository.setTempPassword(actor, tempPassword);
	}

}
