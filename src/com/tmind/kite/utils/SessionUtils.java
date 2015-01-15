package com.tmind.kite.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * <p>Title: Framework </p>
 * <p>Description: Framework</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: tmind.com</p>
 * @author starxu
 * @version 1.0
 * http servlet session utils
 */
public class SessionUtils
{
  public static Object getObjectAttribute(HttpSession session, String objectName)
  {
    return session.getAttribute(objectName);
  }

  public static void setObjectAttribute(HttpSession session, String objectName, Object object)
  {
    session.setAttribute(objectName, object);
  }

  public static void removeObjectAttribute(HttpSession session, String objectName)
  {
    session.removeAttribute(objectName);
  }

  public static Object getObjectAttribute(HttpServletRequest request, String objectName)
  {
    return request.getSession().getAttribute(objectName);
  }

  public static void setObjectAttribute(HttpServletRequest request, String objectName, Object object)
  {
    request.getSession().setAttribute(objectName, object);
  }

  public static void removeObjectAttribute(HttpServletRequest request, String objectName)
  {
    request.getSession().removeAttribute(objectName);
  }

}
