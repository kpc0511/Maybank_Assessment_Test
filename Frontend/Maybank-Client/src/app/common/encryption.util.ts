import CryptoJS from 'crypto-js';

const SECRET_KEY = '8Sw1doXBThPB4G2n38DisSGu79VdWD8g';

function parseKey() {
  return CryptoJS.enc.Utf8.parse(SECRET_KEY);
}

export function encrypt(plainText: string): string {
  const keyParsed = parseKey();
  const cipherText = CryptoJS.AES.encrypt(plainText, keyParsed, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  }).toString();
  return cipherText;
}

export function decrypt(cipherText: string): string {
  const keyParsed = parseKey();
  const bytes = CryptoJS.AES.decrypt(cipherText, keyParsed, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7,
  });
  const originalText = bytes.toString(CryptoJS.enc.Utf8);
  return originalText;
}
