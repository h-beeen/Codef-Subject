package io.codef.subject.application.dto.response;

public record BankAccountResponse(
        String resAccount, // 계좌번호 원본
        String resAccountDisplay, // 계좌번호 '-'로 구분
        String resAccountName, // 계좌이름
        String balance // 잔액
) {
}
