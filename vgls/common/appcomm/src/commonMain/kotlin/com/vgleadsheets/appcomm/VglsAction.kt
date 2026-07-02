package com.vgleadsheets.appcomm

import net.sigmabeta.sage.appcomm.SageAction

open class VglsAction : SageAction() {
    data object RefreshDbClicked : VglsAction()
    data object DbSeeWhatsNewClicked : VglsAction()
    data object AppSeeWhatsNewClicked : VglsAction()

    data object PageClicked : VglsAction()
    data object PageZoomedOutMax : VglsAction()
    data object PageZoomedIn : VglsAction()
}
