import { Directive,HostBinding ,HostListener, ElementRef } from '@angular/core';

@Directive({
  selector: '[appDropDown]'
})
export class DropDownDirective {
  @HostBinding('class.open') isOpen = false;

   @HostListener('document:click', ['$event']) toggleOpen(eventData: Event){
    if (this.elementRef.nativeElement.contains(event.target)){
     this.isOpen = true;
    }else{
      this.isOpen = false;
    }
   }
  constructor(private elementRef : ElementRef) {
   }

}
