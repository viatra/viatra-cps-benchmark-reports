import { Directive , HostListener, HostBinding, ElementRef } from "@angular/core";
@Directive({
    selector: '[appdropdown]'
})
export class DropdownDiretive {
    constructor(private _elementRef : ElementRef) {
    }
    @HostBinding('class.open') isOpen = false;

    @HostListener('click') toogleOpen(){
        if(!this.isOpen){
            this.isOpen = true;
        }
    }
    @HostListener('document:click', ['$event.target'])
    public onClick(targetElement) {
        const clickedInside = this._elementRef.nativeElement.contains(targetElement);
        if (!clickedInside) {
            this.isOpen = false;
        }
    }
}