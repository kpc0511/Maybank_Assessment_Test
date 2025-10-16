export class AppConstants {
  private static API_BASE_URL = 'http://localhost:8100/maybank/v1/rest/';
  private static OAUTH2_URL = AppConstants.API_BASE_URL + 'oauth2/authorization/';
  private static REDIRECT_URL = '?redirect_uri=http://localhost:8081/login';
  public static API_URL = AppConstants.API_BASE_URL + 'api/';
  public static AUTH_API = AppConstants.API_URL + 'auth/';
  public static ENDUSER_API = AppConstants.API_URL + 'user/';
  public static TABLE_API = AppConstants.API_URL + 'table/';
  public static BRANCH_API = AppConstants.API_URL + 'branch/';
  public static GIFT_API = AppConstants.API_URL + 'gift/';
  public static PROMOTION_API = AppConstants.API_URL + 'promotion/';
  public static TERM_API = AppConstants.API_URL + 'term/';
  public static RANK_API = AppConstants.API_URL + 'ranking/';
  public static CREDIT_API = AppConstants.API_URL + 'credit/';
  public static COMPANY_API = AppConstants.API_URL + 'transaction/';
  public static TRANSACTION_API = AppConstants.API_URL + 'transactions/';
  public static SONG_API = AppConstants.API_URL + 'song/';
  public static IMAGE_API = AppConstants.API_URL + 'imageFile/';
  public static GOOGLE_AUTH_URL = AppConstants.OAUTH2_URL + 'google' + AppConstants.REDIRECT_URL;
  public static FACEBOOK_AUTH_URL = AppConstants.OAUTH2_URL + 'facebook' + AppConstants.REDIRECT_URL;
  public static GITHUB_AUTH_URL = AppConstants.OAUTH2_URL + 'github' + AppConstants.REDIRECT_URL;
  public static LINKEDIN_AUTH_URL = AppConstants.OAUTH2_URL + 'linkedin' + AppConstants.REDIRECT_URL;
}
