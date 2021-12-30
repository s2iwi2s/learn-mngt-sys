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
    console.log('event.ctrlKey:', event.ctrlKey);
    this.setNumberValue();
  }

  setNumberValue() {
    const value = this.el.nativeElement.value;
    let trimmedText = value.replace(/[^0-9.]/g, '');
    trimmedText = trimmedText === '' ? '0' : trimmedText;

    this.el.nativeElement.value = this.round(+trimmedText).toFixed(2);
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    // Allow Backspace, tab, end, and home keys
    if (this.specialKeys.indexOf(event.key) !== -1) {
      return;
    }
    if (event.ctrlKey || event.altKey) {
      return;
    }

    let current: string = this.el.nativeElement.value;
    const position: number = this.el.nativeElement.selectionStart;
    const idx: number = current.indexOf('.');

    // console.log('position:', position);
    // console.log('current.length:', current.length);
    // console.log('indexOf:', idx);
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

  round(num: number): number {
    const m = Number((Math.abs(num) * 100).toPrecision(15));
    return (Math.round(m) / 100) * Math.sign(num);
  }
}
