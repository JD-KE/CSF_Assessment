import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartStore } from '../cart.store';
import { Observable, Subscription } from 'rxjs';
import { LineItem, Order } from '../models';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirm-checkout',
  templateUrl: './confirm-checkout.component.html',
  styleUrl: './confirm-checkout.component.css'
})
export class ConfirmCheckoutComponent implements OnInit, OnDestroy {
  
  // TODO Task 3
  private fb = inject(FormBuilder)
  private cartStore = inject(CartStore)
  private productSvc = inject(ProductService)
  private router = inject(Router)

  form!:FormGroup
  cart$!:Observable<LineItem[]>
  cart!:LineItem[]
  totalSub!:Subscription
  totalPrice!:number
  checkoutSub!:Subscription
  
  ngOnInit(): void {
    this.form = this.createForm()
    this.cart$ = this.cartStore.getCart
    this.totalSub = this.cart$.subscribe({
      next: lineItems => {
        this.cart = lineItems
        var total:number = 0
        for (let li of lineItems) {
          total += li.price*li.quantity
        }
        this.totalPrice = total
      }
    })
    
  }

  ngOnDestroy(): void {
    this.totalSub?.unsubscribe()
  }

  processOrder() {
    // this.productSvc.checkout({
    //   ...this.form.value,
    //   cart: this.cart
    // })
    this.checkoutSub = this.productSvc.checkout({
      ...this.form.value,
      cart: this.cart
    } as Order).subscribe({
      next: (value:any) => {
        console.info(value)
        alert('OrderId: ' + value.orderId)
        this.cartStore.clearCart()
        this.router.navigate(['/'])
      },
      error: (error) => {
        console.info(error)
        
        alert(error.error.message)
      },
      complete: () => this.checkoutSub.unsubscribe()
    })
    
  }

  createForm() {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      address: this.fb.control<string>('',[Validators.required, Validators.minLength(3)]),
      priority: this.fb.control<boolean>(false),
      comments: this.fb.control<string>('')
    })
  }




}
