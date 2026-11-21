const { Router } = require('express');
const { xacThuc, choPhep } = require('../../middleware/auth');
const { ApiError } = require('../../shared/http');

const router = Router();

function choTichHop(req, res, next) {
  next(new ApiError(501, 'CHUC_NANG_CHUA_TICH_HOP', 'Chức năng chưa tích hợp.'));
}

router.post('/phieu-muon', xacThuc, choPhep('ADMIN', 'THU_THU'), choTichHop);
router.get('/ban-doc/toi/dang-muon', xacThuc, choPhep('BAN_DOC'), choTichHop);

module.exports = router;
