import { Injectable ,EventEmitter } from '@angular/core';

@Injectable()
export class ComponentService {
  private _componentUpdate : EventEmitter<ComponentUpdateEvent>;
  constructor() {
    this._componentUpdate = new EventEmitter<ComponentUpdateEvent>();
   }

get ComponentUpdate(){
  return this._componentUpdate;
}

updateComponent(component: string, hide: boolean){
  this._componentUpdate.emit(new ComponentUpdateEvent(component,hide));
}

}

export class ComponentUpdateEvent{
  constructor(public component: string, public hide :boolean){}
}