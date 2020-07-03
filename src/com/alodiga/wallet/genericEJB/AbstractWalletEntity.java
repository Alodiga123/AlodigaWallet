package com.alodiga.wallet.genericEJB;

import java.io.Serializable;

public abstract class AbstractWalletEntity implements WalletGenericEntity, Serializable {

	public abstract Object getPk();

	
}
