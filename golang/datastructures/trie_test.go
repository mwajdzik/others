package datastructures

import (
	"reflect"
	"testing"
)

func TestTrieCreation(t *testing.T) {
	trie := NewTrie()

	if trie.root == nil {
		t.Fatalf("root should not be nil")
	}

	if trie.root.isWord == true {
		t.Fatalf("root should not be a word")
	}

	for _, c := range trie.root.children {
		if c != nil {
			t.Fatalf("all children should be nil")
		}
	}
}

func shouldNotBeWordNode(t *testing.T, n *trieNode) {
	if n == nil {
		t.Fatalf("invalid array state")
	}

	if n.isWord == true {
		t.Fatalf("should not be a word")
	}
}

func shouldBeWordNode(t *testing.T, n *trieNode) {
	if n == nil {
		t.Fatalf("invalid array state")
	}

	if n.isWord != true {
		t.Fatalf("should be a word")
	}
}

func TestTrieAddWord(t *testing.T) {
	trie := NewTrie()

	trie.AddWord("cat")
	shouldNotBeWordNode(t, trie.root)

	letterC := trie.root.children['c'-'a']
	shouldNotBeWordNode(t, letterC)

	letterA := letterC.children['a'-'a']
	shouldNotBeWordNode(t, letterA)

	letterT := letterA.children['t'-'a']
	shouldBeWordNode(t, letterT)

	trie.AddWord("car")

	letterR := letterA.children['r'-'a']
	shouldBeWordNode(t, letterR)

	trie.AddWord("cardio")

	letterD := letterR.children['d'-'a']
	shouldNotBeWordNode(t, letterD)

	letterI := letterD.children['i'-'a']
	shouldNotBeWordNode(t, letterI)

	letterO := letterI.children['o'-'a']
	shouldBeWordNode(t, letterO)

	trie.AddWord("card")
	shouldBeWordNode(t, letterD)
}

func TestTrieGetWords(t *testing.T) {
	trie := NewTrie()

	trie.AddWord("cat")
	trie.AddWord("car")
	trie.AddWord("cardio")
	trie.AddWord("card")

	words := trie.GetWords("ca")
	if !reflect.DeepEqual(words, []string{"car", "card", "cardio", "cart"}) {
		t.Fatalf("invalid words")
	}

	words = trie.GetWords("cat")
	if !reflect.DeepEqual(words, []string{"cat"}) {
		t.Fatalf("invalid words")
	}

	words = trie.GetWords("card")
	if !reflect.DeepEqual(words, []string{"card", "cardio"}) {
		t.Fatalf("invalid words")
	}

	words = trie.GetWords("cardiologist")
	if !reflect.DeepEqual(words, []string{}) {
		t.Fatalf("invalid words")
	}
}
