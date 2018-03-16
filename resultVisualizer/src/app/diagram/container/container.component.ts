import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {
  @Input() scale: number;
  @Input() metric: string;
  @Input() default: number;
  @Input() titles: Array<string>
  @Input() checked: Array<{
    "line-through": boolean
  }>
  constructor() { }

  ngOnInit() {
  }

}
