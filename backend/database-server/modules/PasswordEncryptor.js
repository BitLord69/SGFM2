const crypto = require('crypto');

module.exports = (config = {
  // a config object with some defaults here
  // but expected to be changed by the middleware user
  salt: 'defaultSalt'
}) => (req, res, next) => {
  if (!req.body || !req.body.password) { next(); return; }
  req.body.password = crypto.createHmac('sha256', config.salt)
    .update(req.body.password).digest('hex');
  next();
}