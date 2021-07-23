package com.jhs.exam.exam2.service;

import java.util.List;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.ArticleRepository;
import com.jhs.exam.exam2.repository.MemberRepository;
import com.jhs.exam.exam2.util.Ut;

public class MemberService {
	private MemberRepository memberRepository = Container.memberRepository;

	public ResultData login(String loginId, String loginPw) {
		Member member = memberRepository.getMemberByLoginId(loginId);

		if (member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원의 로그인아이디 입니다.");
		}

		if (member.getLoginPw().equals(loginPw) == false) {
			return ResultData.from("F-2", "비밀번호가 일치하지 않습니다.");
		}

		return ResultData.from("S-1", "환영합니다.", "member", member);
	}

	public ResultData join(String loginId, String loginPw, String name, String nickname, String email,
			String cellphoneNo) {
		List<Member> members = memberRepository.getForPrintMembers();
		
		for(Member member : members) {
			if(member.getLoginId().equals(loginId)) {
				return ResultData.from("F-1", Ut.f("이미 사용중인 아이디 입니다."));
			} else if (member.getName().equals(name) && member.getEmail().equals(email)) {
				return ResultData.from("F-2", Ut.f("이미 가입된 회원입니다."));
			}
		}
		
		memberRepository.join(loginId, loginPw, name, nickname, email, cellphoneNo);

		return ResultData.from("S-1", Ut.f("회원가입이 완료되었습니다."));
	}

	public List<Member> getForPrintMembers() {
		return memberRepository.getForPrintMembers();
	}
	
	public ResultData getMemberByNameAndEmail(String name, String email) {
		Member member = memberRepository.getMemberByNameAndEmail(name, email);
			
		if(member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원입니다.");
		}
		
		String loginId = member.getLoginId();
		
		return ResultData.from("S-1", Ut.f("해당 회원의 아이디는 [" + loginId + "] 입니다"), "loginId", loginId);
	}

	public ResultData getMemberByLoginIdAndEmail(String loginId, String email) {
		Member member = memberRepository.getMemberByLoginIdAndEmail(loginId, email);
		
		if(member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원입니다.");
		}
		
		if(member.getEmail().equals(email) == false) {
			return ResultData.from("F-2", "가입하신 이메일이 아닙니다.");
		}
		
		return ResultData.from("S-1", Ut.f("확인 되었습니다."), "member", member);
	}

	public ResultData sendTempLoginPwToEmail(Member actor) {
		// 메일 제목과 내용 만들기
		String tempPassword = Ut.getTempPassword(6);
		
		setTempPassword(actor, tempPassword);
		
		return ResultData.from("S-1", Ut.f("해당 회원의 새로운 비밀번호는 [" + tempPassword + "] 입니다"), "tempPassword", tempPassword);
	}

	private void setTempPassword(Member actor, String tempPassword) {
		memberRepository.setTempPassword(actor, tempPassword);
	}

}
