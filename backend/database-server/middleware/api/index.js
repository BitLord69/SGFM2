const router = require('express').Router()
router.use('/user', require('./user'));
router.use('/auth', require('./auth'));
router.use('/game', require('./game'));
router.use('/league', require('./league'));

module.exports = router