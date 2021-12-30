/* eslint-disable @typescript-eslint/no-inferrable-types */
/* eslint-disable prefer-const */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
/* eslint-disable no-console */
/* eslint-disable @typescript-eslint/prefer-regexp-exec */
/* source: https://stackblitz.com/edit/limit-two-digit-decimal-place?file=package.json */
import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[jhiTwoDigitField]',
})
export class TwoDigitFieldDirective {
  // Allow decimal numbers and negative values
  private regex = /^\d*\.?\d{0,2}$/g;
  // Allow key codes for special events. Reflect :
  // Backspace, tab, end, home
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home', '-', 'ArrowLeft', 'ArrowRight', 'Del', 'Delete'];

  constructor(private el: ElementRef) {}

  @HostListener('change', ['$event'])
  onChange(event: KeyboardEvent) {
    this.setNumberValue();
  }

  setNumberValue() {
    const value = this.el.nativeElement.value;
    let trimmedText = value.replace(/[^0-9.]/g, '');
    trimmedText = trimmedText === '' ? '0' : trimmedText;

    this.el.nativeElement.value = (+trimmedText).toFixed(2);
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    // Allow Backspace, tab, end, and home keys
    if (this.specialKeys.indexOf(event.key) !== -1) {
      return;
    }
    if (event.ctrlKey) {
      if (event.key.toLowerCase() === 'a' || event.key.toLowerCase() === 'c' || event.key.toLowerCase() === 'v') {
        return;
      }
    }

    let current: string = this.el.nativeElement.value;
    const position = this.el.nativeElement.selectionStart;
    const next: string = [current.slice(0, position), event.key === 'Decimal' ? '.' : event.key, current.slice(position)].join('');
    if (next && !String(next).match(this.regex)) {
      event.preventDefault();
    }
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    if (event.clipboardData) {
      const dataToPaste = event.clipboardData.getData('text');
      if (isNaN(parseFloat(dataToPaste))) {
        event.preventDefault();
      }
    }
  }
}
