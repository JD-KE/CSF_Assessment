
// TODO Task 2

import { Injectable } from "@angular/core";
import { Cart, LineItem } from "./models";
import { ComponentStore } from "@ngrx/component-store";

const INIT_STATE: Cart = {
    lineItems:[]
}

// Use the following class to implement your store
@Injectable()
export class CartStore extends ComponentStore<Cart> {

    constructor() {
        super(INIT_STATE)
    }

    readonly addToCart = this.updater<LineItem>(
        (cart:Cart, item:LineItem) => {
            const newCart:Cart = {
                lineItems:[...cart.lineItems, item]
            }
            return newCart
        }
    )

    readonly getCart = this.select(
        (cart:Cart) => cart.lineItems
    )

    readonly getItemNoInCart = this.select(
        (cart:Cart) => {
            // console.info(cart)
            // const noDupeSet = new Set(cart.lineItems)
            // console.info(noDupeSet)
            // const noDupe:LineItem[] = Array.from(noDupeSet)
            // return noDupe.length
            return cart.lineItems.length
        }
    )
}
