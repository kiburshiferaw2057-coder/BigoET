const fs = require('fs');
const path = require('path');

class PaymentConfigService {
  constructor(configPath = path.join(__dirname, '../config/app_config.json')) {
    this.config = JSON.parse(fs.readFileSync(configPath, 'utf8'));
  }

  getPaymentMethodsSummary() {
    return this.config.paymentMethods;
  }

  formatBirrAmount(amount) {
    const numeric = Number(amount || 0);
    return `${numeric.toFixed(2)} ${this.config.paymentMethods.currency || 'Birr'}`;
  }
}

module.exports = PaymentConfigService;
