#![allow(unused)]

use wasm_bindgen::prelude::*;
use std::{cell::RefCell, mem::{self, MaybeUninit}, sync::Once};
use rusty_dumb_tools::calculator::DumbCalculator;



#[wasm_bindgen]
pub fn get_greeting(who: String) -> String {
    format!("Hello, {}!", who)
}


#[wasm_bindgen]
struct Calculator {
    display_width: usize,
    calculator: DumbCalculator,
}
#[wasm_bindgen]
impl Calculator {
    pub fn new(display_width: u8) -> Calculator {
        Calculator {
            display_width: display_width as usize,
            calculator: DumbCalculator::new(),
        }
    }
    pub fn push(&mut self, key: &str) {
        self.calculator.push(key).unwrap();
    }
    pub fn use_angle_mode(&mut self, angle_mode: &str) {
        self.calculator.use_angle_mode(angle_mode);
    }
    pub fn reset(&mut self) {
        self.calculator.reset();
    }
    pub fn undo(&mut self) {
        self.calculator.undo();
    }
    pub fn get_display(&self) -> String {
        self.calculator.get_display_sized(self.display_width)
    }
    pub fn get_history(&self) -> String {
        match self.calculator.get_history_string(true) {
            Some(history) => history,
            None => String::new(),
        }
    }
    pub fn get_indicators(&self) -> String {
        let operator_indicator = get_op_indicator(&self.calculator);
        let bracket_indicator = get_bracket_indicator(&self.calculator);
        let memory = match self.calculator.get_memory() {
            Some(memory) => format!("{}", memory),
            None => "".to_string(),
        };
        format!("{}|{}|{}", operator_indicator, bracket_indicator, memory)
    }
}

// turn the "operator" indicator to something more human readable
fn get_op_indicator(calculator: &DumbCalculator) -> &'static str {
    let operator = calculator.get_last_operator();
    match operator {
        Some(operator) => match operator.as_str() {
            "+" => "+",
            "-" => "-",
            "*" => "x",
            "/" => "÷",
            _ => " ",
        },
        None => "",
    }
}

// turn the "bracket" indicator to something more human readable
fn get_bracket_indicator(calculator: &DumbCalculator) -> &'static str {
    match calculator.count_opened_brackets() {
        1 => "⑴",
        2 => "⑵",
        3 => "⑶",
        4 => "⑷",
        5 => "⑸",
        6 => "⑹",
        7 => "⑺",
        8 => "⑻",
        9 => "⑼",
        10 => "⑽",
        _ => "",
    }
}





// *****************************************
// *** the followings are for experiment ***
// *****************************************


#[wasm_bindgen]
pub fn calc_set_display_width(width: u8) -> String {
    unsafe {
        DISPLAY_WIDTH = width as usize;
    }
    let calculator = get_the_calculator();
    calculator.get_display_sized()
}


static mut DISPLAY_WIDTH: usize = 12;

struct TheCalculator {
    calculator_ref: RefCell<DumbCalculator>,
}
impl TheCalculator {
    fn push(&self, key: &str) {
        let mut calculator = self.calculator_ref.borrow_mut();
        calculator.push(key).unwrap();
    }
    // fn push_chars(&self, keys: &str) {
    //     let mut calculator = self.calculator_ref.borrow_mut();
    //     calculator.push_chars(keys).unwrap();
    // }
    fn undo(&self) {
        let mut calculator = self.calculator_ref.borrow_mut();
        calculator.undo();
    }
    fn reset(&self) {
        let mut calculator = self.calculator_ref.borrow_mut();
        calculator.reset();
    }
    fn get_history(&self) -> String {
        let calculator = self.calculator_ref.borrow();
        calculator.get_history_string(true).unwrap()
    }
    fn get_display(&self) -> String {
        let calculator = self.calculator_ref.borrow();
        calculator.get_display()
    }
    fn get_display_sized(&self) -> String {
        let width = unsafe { DISPLAY_WIDTH };
        let calculator = self.calculator_ref.borrow();
        calculator.get_display_sized(width)
    }
}

fn get_the_calculator() -> &'static TheCalculator {  // https://stackoverflow.com/questions/27791532/how-do-i-create-a-global-mutable-singleton
    // Create an uninitialized static
    static mut SINGLETON: MaybeUninit<TheCalculator> = MaybeUninit::uninit();
    static ONCE: Once = Once::new();
    unsafe {
        ONCE.call_once(|| {
            // Make it
            let singleton = TheCalculator {
                calculator_ref: RefCell::new(DumbCalculator::new())
            };
            // Store it to the static var, i.e. initialize it
            SINGLETON.write(singleton);
        });
        // Now we give out a shared reference to the data, which is safe to use
        // concurrently.
        SINGLETON.assume_init_ref()
    }
}


