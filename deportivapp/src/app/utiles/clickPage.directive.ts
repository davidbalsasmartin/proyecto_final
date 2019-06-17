import {Directive, Output, EventEmitter, HostListener} from '@angular/core';

  @Directive({
      selector: '[clickPage]'
  })
  export class ClickPageDirective {
      constructor() {
      }
  
     @Output()
      public clickPage = new EventEmitter<MouseEvent>();
  
      @HostListener('document:click', ['$event'])
      public onClick(event: MouseEvent): void {
        this.clickPage.emit(event);
      }
  }