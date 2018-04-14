class Input {
  constructor(config) {
    this.el = config.el
    this.rules = config.rules
    this.param = config.param
    this.validation = config.validation
  }
  validate() {

    const field = this.el

    document.querySelectorAll(field).forEach((el) => {
      el.addEventListener('keypress', (event) => {

        let whichCode = event.which
        let keyCode = event.keyCode
        let letter = String.fromCharCode(event.which).toLowerCase()
        let param = this.param

        let exp = this.expression()

        let valid =
          -1 !== exp.indexOf(letter) ||
          9 === keyCode ||
          37 !== whichCode && 37 === keyCode ||
          39 === keyCode && 39 !== whichCode ||
          8 === keyCode ||
          46 === keyCode && 46 !== whichCode

        valid && 161 !== whichCode || event.preventDefault()

      })
    })
  }
  expression() {
    let exp = ''
    if (this.rules === undefined || this.rules === null) {
      if (this.param !== undefined || this.param !== null) {
        switch (this.param) {
          case 'letters':
            exp = 'abcdefghijklmnñopqrstuvwxyz '
            break
          case 'numbers':
            exp = '1234567890'
            break
          case 'date':
            exp = '1234567890/'
            break
          case 'alphanumeric':
            exp = 'abcdefghijklmnñopqrstuvwxyz1234567890'
            break
          default :
            console.error(`Houston we have a problem! by passing a param. Check documentation for more info.`)
        }
      } else {
        console.error(`Houston we have a problem! by passing a param. Check documentation for more info.`)
      }
    } else {
      exp = this.rules
    }
    return exp
  }

}