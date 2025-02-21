import { getCurrentUser } from '@/common/apis/utils/tokenUtils.js'

export const authGuard = (to, from, next) => {
  const isAuthenticated = localStorage.getItem('access_token') !== null;

  if (to.matched.some(record => record.meta.requiresAuth) && !isAuthenticated) {
    // alert('로그인이 필요한 서비스입니다.');
    next({name: 'login'});
    return;
  }

  next();
}

export const passwordVerificationGuard = (to, from, next) => {
  const verificationToken = sessionStorage.getItem('verification_token');

  if (to.matched.some(record => record.meta.requiresPasswordVerification) && !verificationToken) {
    next({
      name: 'passwordCheck',
      query: { redirect: to.fullPath }
    });
    return;
  }

  next();
}


export const ownerGuard = (to, from, next) => {

  const user = getCurrentUser();

  if (user) {
    const userRole = user.role;

    if (to.matched.some(record => record.meta.requiresOwner) && userRole !== "owner") {
      next({ name: 'forbidden' });

      return;
    }
  }

  next();
}
