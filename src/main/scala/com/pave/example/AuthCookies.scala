package com.example

import spray.http._
import Util._

object AuthCookies {
  val CsrfTokenCookieName = "XSRF-TOKEN"
  val XCsrfTokenCookieName = "X-XSRF-TOKEN"

  private val path = Some("/")

  def sessionCookie(session: Session, domain: String, secure: Boolean): HttpCookie = {
      HttpCookie(
        name = CsrfTokenCookieName,
        content = session.id,
        expires = Some(session.expiresOn.toSprayDateTime),
        domain = Some(domain),
        path = path,
        secure = secure
      )
  }

  def removeSessionCookie(domain: String, secure: Boolean): HttpCookie =
      HttpCookie(
        name = CsrfTokenCookieName,
        content = "",
        expires = None,
        domain = Some(domain),
        path = path,
        secure = secure
      )
}

