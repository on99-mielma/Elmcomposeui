package com.on99.elmcomposeui.data

import com.on99.elmcomposeui.R

//<string name="searchbar_ph_00">Mcdonla|good</string>
//<string name="searchbar_ph_01">BurgarKing|Expensive</string>
//<string name="searchbar_ph_02">Subway|eeee</string>
//<string name="searchbar_ph_03">SoftSkin|cheap</string>
//<string name="searchbar_ph_04">DUAL|PS5</string>
//<string name="searchbar_ph_05">BloodBorne|TOP1</string>
//<string name="searchbar_ph_06">DarkSoul|Thankyou</string>
//<string name="searchbar_ph_07">EldenRING|summuary</string>
//<string name="searchbar_ph_08">CSGO|fuckyou</string>


class searchbar_ph {
    val phList = mutableListOf<String>(
        "Mcdonla|good",
        "BurgarKing|Expensive",
        "Subway|eeee",
        "SoftSkin|cheap",
        "DUAL|PS5",
        "BloodBorne|TOP1",
        "DarkSoul|Thankyou",
        "EldenRING|summuary",
        "CSGO|fuckyou"
    )

    fun getList(): MutableList<String> {
        return phList
    }

    fun getRandomOne(): String {
        val len = this.phList.size
        val randomInt = (0..len - 1).random()
        return this.phList.get(randomInt)
    }
    fun size():Int{
        return this.phList.size
    }

    fun refrashList() {
        /*
        TODO
         */
    }
}