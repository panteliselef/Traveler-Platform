'use strict';

function Validator() {
	const USERNAME_REGEX = '^[A-Za-z]{8,}$';
	const EMAIL_REGEX = '^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,}$';
  const PASSWORD_REGEX = '^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#?!$%^&+=])(?=\\S+$).{8,10}$' //aaZZa44@
	const NAME_REGEX = '^([a-zA-Z]){3,15}$';
	const CITY_REGEX = '^([a-zA-Z]){2,20}$';
	const JOB_REGEX = '^([\sa-zA-Z]){3,15}$';

	this.testUsername = (str) => {
		return new RegExp(USERNAME_REGEX).test(str);
	};

	this.testPassword = (str) => {
		return new RegExp(PASSWORD_REGEX).test(str);
	};

	this.testEmail = (str) => {
		return new RegExp(EMAIL_REGEX).test(str);
	};
	this.testName = (str) => {
    return new RegExp(NAME_REGEX).test(str);
  };
	this.testCity = (str) => {
		return new RegExp(CITY_REGEX).test(str);
  };
  this.testOccupation = (str) => {
		return new RegExp(JOB_REGEX).test(str);
	};
}
