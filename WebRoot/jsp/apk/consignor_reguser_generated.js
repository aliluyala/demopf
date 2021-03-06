// automatically generated by the FlatBuffers compiler, do not modify

/**
 * @const
*/
var com = com || {};

/**
 * @const
*/
com.hyt258 = com.hyt258 || {};

/**
 * @const
*/
com.hyt258.hyt = com.hyt258.hyt || {};

/**
 * @const
*/
com.hyt258.hyt.protocol = com.hyt258.hyt.protocol || {};

/**
 * @constructor
 */
com.hyt258.hyt.protocol.ConsignorRegUser = function() {
  /**
   * @type {flatbuffers.ByteBuffer}
   */
  this.bb = null;

  /**
   * @type {number}
   */
  this.bb_pos = 0;
};

/**
 * @param {number} i
 * @param {flatbuffers.ByteBuffer} bb
 * @returns {com.hyt258.hyt.protocol.ConsignorRegUser}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.__init = function(i, bb) {
  this.bb_pos = i;
  this.bb = bb;
  return this;
};

var factorial = function(n) {
    
    if (n < 0)
        
        return;
    
    if (n === 0)
        
        return 1;
    
    return n * factorial(n - 1)
    
};

/**
 * @param {flatbuffers.ByteBuffer} bb
 * @param {com.hyt258.hyt.protocol.ConsignorRegUser=} obj
 * @returns {com.hyt258.hyt.protocol.ConsignorRegUser}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.getRootAsConsignorRegUser = function(bb, obj) {
  return (obj || new com.hyt258.hyt.protocol.ConsignorRegUser).__init(bb.readInt32(bb.position()) + bb.position(), bb);
};

/**
 * @param {com.hyt258.hyt.protocol.Result=} obj
 * @returns {com.hyt258.hyt.protocol.Result}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.res = function(obj) {
  var offset = this.bb.__offset(this.bb_pos, 4);
  return offset ? (obj || new com.hyt258.hyt.protocol.Result).__init(this.bb.__indirect(this.bb_pos + offset), this.bb) : null;
};

/**
 * @returns {number}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.userId = function() {
  var offset = this.bb.__offset(this.bb_pos, 6);
  return offset ? this.bb.readUint32(this.bb_pos + offset) : 0;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.mobileNo = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 8);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.avartar = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 10);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.token = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 12);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.realName = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 14);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.idCard = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 16);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.deviceId = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 18);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.jpushId = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 20);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.cities = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 22);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.cargoType = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 24);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.address = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 26);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.fax = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 28);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.isAuth = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 30);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @param {flatbuffers.Encoding=} optionalEncoding
 * @returns {string|Uint8Array}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.isPerfect = function(optionalEncoding) {
  var offset = this.bb.__offset(this.bb_pos, 32);
  return offset ? this.bb.__string(this.bb_pos + offset, optionalEncoding) : null;
};

/**
 * @returns {boolean}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.forceUpdate = function() {
  var offset = this.bb.__offset(this.bb_pos, 34);
  return offset ? !!this.bb.readInt8(this.bb_pos + offset) : false;
};

/**
 * @returns {number}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.rank = function() {
  var offset = this.bb.__offset(this.bb_pos, 36);
  return offset ? this.bb.readFloat64(this.bb_pos + offset) : 0;
};

/**
 * @returns {number}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.totalOrders = function() {
  var offset = this.bb.__offset(this.bb_pos, 38);
  return offset ? this.bb.readUint32(this.bb_pos + offset) : 0;
};

/**
 * @returns {number}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.waybillMsgCount = function() {
  var offset = this.bb.__offset(this.bb_pos, 40);
  return offset ? this.bb.readUint32(this.bb_pos + offset) : 0;
};

/**
 * @returns {number}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.orderMsgCount = function() {
  var offset = this.bb.__offset(this.bb_pos, 42);
  return offset ? this.bb.readUint32(this.bb_pos + offset) : 0;
};

/**
 * @returns {number}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.authStep = function() {
  var offset = this.bb.__offset(this.bb_pos, 44);
  return offset ? this.bb.readUint32(this.bb_pos + offset) : 0;
};

/**
 * @returns {boolean}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.prototype.isExpired = function() {
  var offset = this.bb.__offset(this.bb_pos, 46);
  return offset ? !!this.bb.readInt8(this.bb_pos + offset) : false;
};

/**
 * @param {flatbuffers.Builder} builder
 */
com.hyt258.hyt.protocol.ConsignorRegUser.startConsignorRegUser = function(builder) {
  builder.startObject(22);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} resOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addRes = function(builder, resOffset) {
  builder.addFieldOffset(0, resOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {number} userId
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addUserId = function(builder, userId) {
  builder.addFieldInt32(1, userId, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} mobileNoOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addMobileNo = function(builder, mobileNoOffset) {
  builder.addFieldOffset(2, mobileNoOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} avartarOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addAvartar = function(builder, avartarOffset) {
  builder.addFieldOffset(3, avartarOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} tokenOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addToken = function(builder, tokenOffset) {
  builder.addFieldOffset(4, tokenOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} realNameOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addRealName = function(builder, realNameOffset) {
  builder.addFieldOffset(5, realNameOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} idCardOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addIdCard = function(builder, idCardOffset) {
  builder.addFieldOffset(6, idCardOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} deviceIdOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addDeviceId = function(builder, deviceIdOffset) {
  builder.addFieldOffset(7, deviceIdOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} jpushIdOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addJpushId = function(builder, jpushIdOffset) {
  builder.addFieldOffset(8, jpushIdOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} citiesOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addCities = function(builder, citiesOffset) {
  builder.addFieldOffset(9, citiesOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} cargoTypeOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addCargoType = function(builder, cargoTypeOffset) {
  builder.addFieldOffset(10, cargoTypeOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} addressOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addAddress = function(builder, addressOffset) {
  builder.addFieldOffset(11, addressOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} faxOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addFax = function(builder, faxOffset) {
  builder.addFieldOffset(12, faxOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} isAuthOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addIsAuth = function(builder, isAuthOffset) {
  builder.addFieldOffset(13, isAuthOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} isPerfectOffset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addIsPerfect = function(builder, isPerfectOffset) {
  builder.addFieldOffset(14, isPerfectOffset, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {boolean} forceUpdate
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addForceUpdate = function(builder, forceUpdate) {
  builder.addFieldInt8(15, +forceUpdate, +false);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {number} rank
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addRank = function(builder, rank) {
  builder.addFieldFloat64(16, rank, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {number} totalOrders
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addTotalOrders = function(builder, totalOrders) {
  builder.addFieldInt32(17, totalOrders, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {number} waybillMsgCount
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addWaybillMsgCount = function(builder, waybillMsgCount) {
  builder.addFieldInt32(18, waybillMsgCount, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {number} orderMsgCount
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addOrderMsgCount = function(builder, orderMsgCount) {
  builder.addFieldInt32(19, orderMsgCount, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {number} authStep
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addAuthStep = function(builder, authStep) {
  builder.addFieldInt32(20, authStep, 0);
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {boolean} isExpired
 */
com.hyt258.hyt.protocol.ConsignorRegUser.addIsExpired = function(builder, isExpired) {
  builder.addFieldInt8(21, +isExpired, +false);
};

/**
 * @param {flatbuffers.Builder} builder
 * @returns {flatbuffers.Offset}
 */
com.hyt258.hyt.protocol.ConsignorRegUser.endConsignorRegUser = function(builder) {
  var offset = builder.endObject();
  return offset;
};

/**
 * @param {flatbuffers.Builder} builder
 * @param {flatbuffers.Offset} offset
 */
com.hyt258.hyt.protocol.ConsignorRegUser.finishConsignorRegUserBuffer = function(builder, offset) {
  builder.finish(offset);
};

// Exports for Node.js and RequireJS
this.com = com;
