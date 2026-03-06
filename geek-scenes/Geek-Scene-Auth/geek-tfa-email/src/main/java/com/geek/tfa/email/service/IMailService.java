package com.geek.tfa.email.service;

import com.geek.auth.common.service.OauthVerificationCodeService;
import com.geek.auth.common.service.TfaService;

public interface IMailService extends OauthVerificationCodeService,TfaService {
}
