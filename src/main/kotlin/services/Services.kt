package org.example.dependent.kts.services

import org.example.dependent.kts.domain.Good
import org.springframework.stereotype.Service

@Service
class Warehouse {
    fun isGoodInStock(good: Good): Boolean {
        with(good) {
           return price < 100 && name != "Burger"
        }
    }
}

@Service
class Lawyer {
    fun isGoodLegal(good: Good): Boolean {
        return good.name != "Drugs"
    }
}

class SellingJournal {
    fun logGood(good: Good) {
        with(good) {
            println("[SellingJournal] $name has been sold for $price units")
        }
    }
}