const System = Java.type('java.lang.System')
const SimpleThrustWorkerManager = Java.type('br.com.softbox.thrust.api.thread.simple.SimpleThrustWorkerManager')
const ThrustContextAPI = Java.type('br.com.softbox.thrust.api.ThrustContextAPI')

const initManager = () => {
    manager.initPool(1, 2, __ROOT_DIR__)
}

console.log('simple: main.')
const manager = new SimpleThrustWorkerManager()
let ok = false
initManager()
try {
    initManager()
    ok = true
} catch (e) {
    console.log('simple: confirmed failed: ', e)
}
if (ok) {
    throw new Error('Cannot continue')
}
console.log('simple: running script')
manager.runScript(`${__ROOT_DIR__}/script01.js`)
console.log('simple: waiting scripts')
manager.waitActiveWorkers(1045)
console.log('simple: continue.', manager.getWorkers().size())
const v = ThrustContextAPI.getValue('simple-value')
console.log('simple: vale:', v)
System.exit(0)
